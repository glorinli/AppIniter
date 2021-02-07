package xyz.glorin.appiniter_lib.multithread

import android.os.Handler
import android.os.HandlerThread

class TaskDispatcher : HandlerThread("TaskDispatcher") {
    private lateinit var handler: Handler

    override fun start() {
        super.start()

        handler = Handler(looper)
    }

    fun runTask(runnable: Runnable) {
        handler.post(runnable)
    }
}