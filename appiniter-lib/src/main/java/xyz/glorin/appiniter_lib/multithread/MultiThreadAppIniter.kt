package xyz.glorin.appiniter_lib.multithread

import android.os.SystemClock
import xyz.glorin.appiniter_lib.AbstractAppIniter

class MultiThreadAppIniter : AbstractAppIniter(), TaskStatusManager.Listener {
    private val mainThreadExecuter = MainThreadTaskExecuter()
    private val threadedTaskExecuter = ThreadedTaskExecuter()
    private val allCompleteSignal = AllCompleteSignal()

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

        allCompleteSignal.waitForAllComplete()

        listener?.onEndExecute(SystemClock.uptimeMillis() - start)

        TaskDispatcher.quitSafely()
        TaskStatusManager.removeListener(this)
    }

    override fun onTaskComplete(identifier: String, costMillis: Long) {
        listener?.onTaskComplete(identifier, costMillis)
        mainThreadExecuter.notifyTaskComplete(identifier)
        threadedTaskExecuter.notifyTaskComplete(identifier)

        if (TaskStatusManager.allComplete(tasks)) {
            allCompleteSignal.notifyAllComplete()
        }
    }
}