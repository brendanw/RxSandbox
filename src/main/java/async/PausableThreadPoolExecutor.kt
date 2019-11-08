package async

import java.util.concurrent.*
import java.util.concurrent.locks.ReentrantLock

interface PausableExecutor {
    fun pause()
    fun resume()
}

/**
 * Implementation from https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/ThreadPoolExecutor.html
 */
class PausableThreadPoolExecutor(corePoolSize: Int,
                                 maxThreads: Int,
                                 keepAlive: Long,
                                 timeUnit: TimeUnit,
                                 blockingQueue: BlockingQueue<Runnable>,
                                 handler: RejectedExecutionHandler)
    : ThreadPoolExecutor(corePoolSize, maxThreads, keepAlive, timeUnit, blockingQueue, handler), PausableExecutor {
    companion object {

        fun newFixedThreadPool(nThreads: Int, handler: RejectedExecutionHandler): PausableThreadPoolExecutor {
            return PausableThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS,
                    LinkedBlockingQueue(), handler)
        }

        fun newCachedThreadPool(): PausableThreadPoolExecutor {
            return PausableThreadPoolExecutor(0,
                    Integer.MAX_VALUE,
                    60L,
                    TimeUnit.SECONDS,
                    SynchronousQueue(),
                    DiscardOldestPolicy())
        }
    }

    private var isPaused: Boolean = false
    private val pauseLock = ReentrantLock()
    private val unpaused = pauseLock.newCondition()

    override fun beforeExecute(thread: Thread, runnabe: Runnable) {
        super.beforeExecute(thread, runnabe)
        pauseLock.lock()
        try {
            while (isPaused) {
                unpaused.await()
            }
        } catch (ie: InterruptedException) {
            thread.interrupt()
        } finally {
            pauseLock.unlock()
        }
    }

    override fun pause() {
        pauseLock.lock()
        try {
            isPaused = true
        } finally {
            pauseLock.unlock()
        }
    }

    override fun resume() {
        pauseLock.lock()
        try {
            isPaused = false
            unpaused.signalAll()
        } finally {
            pauseLock.unlock()
        }
    }
}
