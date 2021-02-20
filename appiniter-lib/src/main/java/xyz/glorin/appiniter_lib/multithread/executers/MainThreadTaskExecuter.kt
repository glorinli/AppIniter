package xyz.glorin.appiniter_lib.multithread.executers

import android.os.SystemClock
import androidx.annotation.WorkerThread
import xyz.glorin.appiniter_lib.InitTask
import xyz.glorin.appiniter_lib.multithread.AllCompleteSignalTask
import xyz.glorin.appiniter_lib.multithread.TaskCompleteListener
import xyz.glorin.appiniter_lib.multithread.TaskExecuter
import java.util.concurrent.LinkedBlockingDeque

class MainThreadTaskExecuter(private val completeListener: TaskCompleteListener) : TaskExecuter {
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
                completeListener.onTaskComplete(
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