package xyz.glorin.appiniter_lib.multithread

import android.os.SystemClock

class MainThreadTaskExecuter : AbstractTaskExecuter() {
    override fun start() {
        super.start()
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
    }
}