package xyz.glorin.appiniter

import android.app.Application
import android.util.Log
import xyz.glorin.appiniter_lib.AppIniter

class DemoApplication : Application() {
    private val initerListener: AppIniter.Listener = object : AppIniter.Listener {
        override fun onStartExecute() {
        }

        override fun onEndExecute(costMillis: Long) {
            Log.i(TAG, "Initer executed, costed: $costMillis ms")
        }

        override fun onTaskComplete(identifier: String, costMillis: Long) {
            Log.i(TAG, "Init task complete: $identifier, cost: $costMillis ms")
        }

    }

    override fun onCreate() {
        super.onCreate()

        val initer = AppIniter.get()
        initer.listener = initerListener
        initer.addTask(FrescoTask())
        initer.addTask(AppDTask())
        initer.addTask(LogTask())
        initer.addTask(GlideTask())
        initer.run()
    }

    companion object {
        private const val TAG = "DemoApplication"
    }
}