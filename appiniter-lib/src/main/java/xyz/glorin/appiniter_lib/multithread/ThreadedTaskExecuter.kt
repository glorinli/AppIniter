package xyz.glorin.appiniter_lib.multithread

import android.os.SystemClock
import xyz.glorin.appiniter_lib.DebugLog

class ThreadedTaskExecuter: AbstractTaskExecuter() {
    override fun start() {
        super.start()

        Thread(ExecuteRunnable()).start()
    }

    inner class ExecuteRunnable : Runnable {
        override fun run() {
            while (!allComplete()) {
                nextExecutableTask()?.let {
                    val start = SystemClock.uptimeMillis()
                    it.run()
                    TaskStatusManager.handleTaskCompleted(
                        it.identifier,
                        SystemClock.uptimeMillis() - start
                    )
                }
            }

            DebugLog.d("ThreadedTaskExecuter", "All completed")
        }
    }
}