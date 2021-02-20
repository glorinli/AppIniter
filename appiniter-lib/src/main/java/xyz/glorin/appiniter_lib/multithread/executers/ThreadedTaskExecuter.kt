package xyz.glorin.appiniter_lib.multithread.executers

import android.os.SystemClock
import xyz.glorin.appiniter_lib.InitTask
import xyz.glorin.appiniter_lib.multithread.TaskCompleteListener
import xyz.glorin.appiniter_lib.multithread.TaskExecuter
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class ThreadedTaskExecuter(private val completeListener: TaskCompleteListener) : TaskExecuter {
    private val executer = ThreadPoolExecutor(
        CORE_POOL_SIZE,
        MAXIMUM_POOL_SIZE,
        KEEP_ALIVE_SECONDS,
        TimeUnit.SECONDS,
        LinkedBlockingQueue()
    )

    override fun start() {
        // Noops
    }

    override fun runTask(task: InitTask) {
        executer.execute {
            val start = SystemClock.uptimeMillis()
            task.run()
            completeListener.onTaskComplete(
                task.identifier,
                SystemClock.uptimeMillis() - start
            )
        }
    }

    companion object {
        private const val CORE_POOL_SIZE = 2
        private const val MAXIMUM_POOL_SIZE = 2
        private const val KEEP_ALIVE_SECONDS = 3L
    }
}