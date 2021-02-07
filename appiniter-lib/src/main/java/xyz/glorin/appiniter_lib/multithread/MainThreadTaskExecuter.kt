package xyz.glorin.appiniter_lib.multithread

import android.os.SystemClock
import androidx.annotation.WorkerThread
import xyz.glorin.appiniter_lib.InitTask
import java.util.concurrent.LinkedBlockingDeque

class MainThreadTaskExecuter : TaskExecuter {
    private val queue = LinkedBlockingDeque<InitTask>()

    override fun start() {
        while (true) {
            val task = queue.take()
            if (task is AllCompleteSignalTask) {
                break
            }

            task?.let {
                val start = SystemClock.uptimeMillis()
                it.run()
                TaskStatusManager.handleTaskCompleted(
                    it.identifier,
                    SystemClock.uptimeMillis() - start
                )
            }

        }
    }

    @WorkerThread
    override fun runTask(task: InitTask) {
        queue.offer(task)
    }
}