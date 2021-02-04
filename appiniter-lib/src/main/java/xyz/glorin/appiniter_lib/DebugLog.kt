package xyz.glorin.appiniter_lib

import android.util.Log

object DebugLog {
    private const val TAG = "AppIniter"

    fun d(tag: String, message: String, t: Throwable? = null) {
        Log.d(TAG, "[$tag]: $message", t)
    }
}