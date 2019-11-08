package async;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A BlockingQueue for KSExecutorService. This is just a priority blocking queue that has a
 * subqueue for each priority. It will always take an action from a higher priority queue if
 * possible.
 *
 * Null elements are not allowed, as null is used as a sentinel value to indicate failure of poll
 * operations.
 *
 * This is not a fully functioning queue and should only be used in ThreadPoolExecutors. All methods
 * not used in this context will fail fast.
 */
public final class KSPriorityQueue<E extends Runnable>
        implements
        BlockingQueue<E> {
    private final HashMap<Integer, Queue<E>> priorityQueueMap;
    private final AtomicInteger count = new AtomicInteger(0);

    private final ReentrantLock takeLock = new ReentrantLock();
    private final ReentrantLock putLock = new ReentrantLock();
    private final Condition notEmpty = takeLock.newCondition();

    KSPriorityQueue() {
        priorityQueueMap = new HashMap<Integer, Queue<E>>(Priority.values().length);
        for (Priority priority : Priority.values()) {
            Queue<E> subqueue = new ConcurrentLinkedQueue<>();
            priorityQueueMap.put(priority.ordinal(), subqueue);
        }
    }

    /**
     * Inserts the specified element into this queue if it is possible to do so immediately.
     *
     * @param task an element to enqueue. Cannot be null
     * @return true if the element was added to the queue, else false
     */
    @Override
    public boolean offer(E task) {
        if (task == null) {
            throw new NullPointerException();
        }
        int isSuccess = -1; // negative unless successful
        putLock.lock();
        try {
            int taskPriority = getPriorityFromTask(task);
            boolean reachedPriority;
            for (int i = 0; i < priorityQueueMap.size(); i++) {
                reachedPriority = i == taskPriority;
                if (reachedPriority && priorityQueueMap.get(taskPriority).offer(task)) {
                    isSuccess = count.getAndIncrement();
                    break;
                }
            }
        } finally {
            putLock.unlock();
        }
        if (isSuccess == 0) {
            takeLock.lock();
            try {
                notEmpty.signal();
            } finally {
                takeLock.unlock();
            }
        }
        return isSuccess >= 0;
    }

    private int getPriorityFromTask(E task) {
        if (task instanceof Prioritized) {
            return ((Prioritized) task).getPriority();
        }
        return Priority.DEFAULT.ordinal();
    }

    /**
     * Retrieves, but does not remove, the highest priority action, or returns null if this queue is
     * empty.
     *
     * <p>
     * This method will discard canceled actions. If all actions are canceled then the queue is
     * considered empty.
     *
     * @return the head of this queue, or null if this queue is empty
     */
    @Override
    public E peek() {
        if (count.get() > 0) {
            takeLock.lock();
            try {
                for (int i = 0; i < priorityQueueMap.size(); i++) {
                    Queue<E> queue = priorityQueueMap.get(i);
                    E action = queue.peek();
                    while (action != null) {
                        if (action instanceof Cancellable && ((Cancellable) action).isCancelled()) {
                            try {
                                queue.poll(); // discard
                                count.getAndDecrement();
                            } catch (NoSuchElementException ex) {
                                //Timber.e(ex, "CANCELLED action was already removed.");
                            }
                            action = queue.peek();
                        } else {
                            return action;
                        }
                    }
                }
            } finally {
                takeLock.unlock();
            }
        }
        return null; // all the actions were canceled
    }

    /**
     * Retrieves and removes the highest priority action, or returns null if this queue is empty.
     *
     * @return the head of this queue, or null if this queue is empty
     */
    @Override
    public E poll() {
        if (count.get() == 0) {
            return null;
        }
        takeLock.lock();
        try {
            if (count.get() > 0) {
                E action;
                for (int i = 0; i < priorityQueueMap.size(); i++) {
                    Queue<E> queue = priorityQueueMap.get(i);
                    action = queue.poll();
                    while (action != null) {
                        if (action instanceof Cancellable && ((Cancellable) action).isCancelled()) {
                            try {
                                action = queue.poll(); // discard
                                count.getAndDecrement();
                            } catch (NoSuchElementException ex) {
                                //Timber.e(ex, "canceled action was gone already");
                            }
                        } else {
                            if (count.decrementAndGet() > 0) {
                                notEmpty.signal();
                            }
                            return action;
                        }
                    }
                }
            }
        } finally {
            takeLock.unlock();
        }
        return null; // all the actions were canceled
    }

    /**
     * Retrieves and removes the head of this queue, waiting if necessary until an element becomes
     * available.
     *
     * @return the head of this queue
     * @throws InterruptedException if interrupted while waiting
     */
    @Override
    public E take() throws InterruptedException {
        takeLock.lockInterruptibly();
        try {
            try {
                while (count.get() == 0) {
                    notEmpty.await();
                }
            } catch (InterruptedException ie) {
                notEmpty.signal(); // propagate to a non interrupted thread
                throw ie;
            }
            E action;
            for (int i = 0; i < priorityQueueMap.size(); i++) {
                Queue<E> queue = priorityQueueMap.get(i);
                action = queue.poll();
                while (action != null) {
                    if (action instanceof Cancellable && ((Cancellable) action).isCancelled()) {
                        try {
                            action = queue.poll();
                            count.getAndDecrement();
                        } catch (NoSuchElementException ex) {
                            //Timber.e(ex, "CANCELLED action was already removed");
                        }
                    } else {
                        if (count.decrementAndGet() > 0) {
                            notEmpty.signal();
                        }
                        return action;
                    }
                }
            }
        } finally {
            takeLock.unlock();
        }
        return null; // all actions were canceled
    }

    /**
     * This method is only used internally by ThreadPoolExecutor to purge the queue. This is because
     * for some BlockingQueue implementations, it is faster than looping poll(). In our case we will
     * handle this ourselves.
     */
    @Override
    public int drainTo(Collection<? super E> c) {
        putLock.lock();
        takeLock.lock();
        try {
            for (int i = 0; i < priorityQueueMap.size(); i++) {
                Queue<E> queue = priorityQueueMap.get(i);
                queue.clear();
            }
            count.set(0);
        } finally {
            takeLock.unlock();
            putLock.unlock();
        }
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return peek() == null;
    }

    /**
     * This queue is unbounded.
     */
    @Override
    public int remainingCapacity() {
        return Integer.MAX_VALUE;
    }



    /**
     * The following methods are not used by KSExecutorService and should fail fast if called.
     */
    @Override
    public final boolean add(E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final void put(E e) throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    @Override
    public final E poll(long timeout, TimeUnit unit) throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final int drainTo(Collection<? super E> c, int maxElements) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final E remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final E element() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean addAll(Collection<? extends E> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean containsAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean removeAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean retainAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final int size() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final <T> T[] toArray(T[] array) {
        throw new UnsupportedOperationException();
    }
}