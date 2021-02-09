package xyz.glorin.appiniter_lib.multithread

import android.os.Handler
import android.os.HandlerThread
import android.os.SystemClock
import xyz.glorin.appiniter_lib.DebugLog

class TaskDispatcher : HandlerThread("TaskDispatcher") {
    private lateinit var handler: Handler

    override fun start() {
        val start = SystemClock.uptimeMillis()
        super.start()

        handler = Handler(looper)
        DebugLog.d("TaskDispatcher", "Start dispatcher, time cost: ${SystemClock.uptimeMillis() - start}")
    }

    fun runTask(runnable: Runnable) {
        handler.post(runnable)
    }
}