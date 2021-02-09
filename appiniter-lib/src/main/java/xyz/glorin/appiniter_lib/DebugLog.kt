package xyz.glorin.appiniter_lib

import android.util.Log

object DebugLog {
    var logEnabled: Boolean = true
    private const val TAG = "AppIniter"

    fun d(tag: String, message: String, t: Throwable? = null) {
        if (logEnabled) {
            Log.d(TAG, "[$tag]: $message", t)
        }
    }
}