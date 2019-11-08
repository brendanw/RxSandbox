package async;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Defines a fixed thread pool (threads are never killed)
 */
public class KSExecutorService extends ThreadPoolExecutor implements PausableExecutor {
    private static final String TAG = KSExecutorService.class.getSimpleName();
    private static final AtomicInteger threadNum = new AtomicInteger(0);

    private boolean isPaused;
    private ReentrantLock pauseLock = new ReentrantLock();
    private Condition unpaused = pauseLock.newCondition();

    private KSExecutorService(int corePoolSize, int maxThreads, long keepAlive,
                              TimeUnit timeUnit, BlockingQueue<Runnable> queue) {
        super(corePoolSize, maxThreads, keepAlive, timeUnit, queue);
    }

    public static KSExecutorService newComputationExecutor(int poolSize) {
        return new KSExecutorService(poolSize,
                poolSize,
                Long.MAX_VALUE,
                TimeUnit.NANOSECONDS,
                new KSPriorityQueue<>());
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
        return new ActionFutureTask<>(runnable, value);
    }

    @Override
    protected void beforeExecute(Thread thread, Runnable runnable) {
        thread.setName("KSComputationThread-" + threadNum.getAndIncrement());
        super.beforeExecute(thread, runnable);
        pauseLock.lock();
        try {
            while (isPaused) {
                unpaused.await();
            }
        } catch (InterruptedException ie) {
            thread.interrupt();
        } finally {
            pauseLock.unlock();
        }
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        return super.newTaskFor(callable);
        /*throw new AbstractMethodError(
                "Should use E extends Runnable & async.Cancellable & async.Prioritized, i.e. Action, rather than a callable.");*/
    }

    void setThreadCount(int threadCount) {
        setCorePoolSize(threadCount);
        setMaximumPoolSize(threadCount);
    }

    @Override
    public void pause() {
        pauseLock.lock();
        try {
            isPaused = true;
        } finally {
            pauseLock.unlock();
        }
    }

    @Override
    public void resume() {
        pauseLock.lock();
        try {
            isPaused = false;
            unpaused.signalAll();
        } finally {
            pauseLock.unlock();
        }
    }
}