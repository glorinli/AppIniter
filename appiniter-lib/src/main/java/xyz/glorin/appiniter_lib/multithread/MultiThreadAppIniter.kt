package xyz.glorin.appiniter_lib.multithread

import android.os.SystemClock
import xyz.glorin.appiniter_lib.AbstractAppIniter

class MultiThreadAppIniter : AbstractAppIniter(), TaskStatusManager.Listener {
    private val mainThreadExecuter = MainThreadTaskExecuter()
    private val threadedTaskExecuter = ThreadedTaskExecuter()

    override fun run() {
        listener?.onStartExecute()

        TaskDispatcher.start()

        TaskStatusManager.addListener(this)
        val start = SystemClock.uptimeMillis()

        if (tasks.isEmpty()) {
            return
        }

        tasks.forEach {
            if (it.requireMainThread) {
                mainThreadExecuter.addTask(it)
            } else {
                threadedTaskExecuter.addTask(it)
            }
        }

        threadedTaskExecuter.start()
        mainThreadExecuter.start()

        listener?.onEndExecute(SystemClock.uptimeMillis() - start)

        TaskDispatcher.quitSafely()
    }

    override fun onTaskComplete(identifier: String, costMillis: Long) {
        listener?.onTaskComplete(identifier, costMillis)
        mainThreadExecuter.notifyTaskComplete(identifier)
        threadedTaskExecuter.notifyTaskComplete(identifier)
    }
}