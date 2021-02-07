package xyz.glorin.appiniter_lib.multithread

import android.os.AsyncTask
import android.os.SystemClock
import xyz.glorin.appiniter_lib.InitTask

class ThreadedTaskExecuter : TaskExecuter {
    private val executer = AsyncTask.THREAD_POOL_EXECUTOR

    override fun start() {
        // Noops
    }

    override fun runTask(task: InitTask) {
        executer.execute {
            val start = SystemClock.uptimeMillis()
            task.run()
            TaskStatusManager.handleTaskCompleted(
                task.identifier,
                SystemClock.uptimeMillis() - start
            )
        }
    }

}