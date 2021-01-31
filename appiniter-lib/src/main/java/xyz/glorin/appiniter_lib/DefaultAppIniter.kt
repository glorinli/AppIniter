package xyz.glorin.appiniter_lib

import android.os.SystemClock

class DefaultAppIniter : AppIniter {
    override var listener: AppIniter.Listener? = null
    private val tasks = mutableListOf<InitTask>()

    override fun addTask(task: InitTask) {
        tasks.add(task)
    }

    override fun run() {
        val start = SystemClock.uptimeMillis()
        listener?.onStartExecute()
        tasks.forEach {
            it.run()
        }
        listener?.onEndExecute(SystemClock.uptimeMillis() - start)
    }
}