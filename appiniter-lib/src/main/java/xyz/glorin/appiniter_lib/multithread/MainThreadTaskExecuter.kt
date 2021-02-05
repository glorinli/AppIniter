package xyz.glorin.appiniter_lib.multithread

import android.os.SystemClock
import xyz.glorin.appiniter_lib.DebugLog

class MainThreadTaskExecuter : AbstractTaskExecuter() {
    override fun start() {
        super.start()
        while (!allComplete()) {
            DebugLog.d("MainThreadTaskExecuter", "wait next task")
            nextExecutableTask()?.let {
                val start = SystemClock.uptimeMillis()
                it.run()
                TaskStatusManager.handleTaskCompleted(
                    it.identifier,
                    SystemClock.uptimeMillis() - start
                )
            }
        }

        DebugLog.d("MainThreadTaskExecuter", "All completed")
    }
}