package xyz.glorin.appiniter_lib.simple

import android.os.SystemClock
import xyz.glorin.appiniter_lib.AbstractAppIniter
import xyz.glorin.appiniter_lib.InitTask

class SimpleAppIniter : AbstractAppIniter() {
    override fun run() {
        val start = SystemClock.uptimeMillis()
        listener?.onStartExecute()
        tasks.forEach {
            val taskStart = SystemClock.uptimeMillis()
            it.run()
            listener?.onTaskComplete(it.identifier, SystemClock.uptimeMillis() - taskStart)
        }
        listener?.onEndExecute(SystemClock.uptimeMillis() - start)
    }
}