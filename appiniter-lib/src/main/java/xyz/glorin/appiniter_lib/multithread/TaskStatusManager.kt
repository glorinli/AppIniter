package xyz.glorin.appiniter_lib.multithread

import xyz.glorin.appiniter_lib.DebugLog
import java.util.concurrent.ConcurrentHashMap

object TaskStatusManager {
    private const val TAG = "TaskStatusManager"

    private val listeners = mutableListOf<Listener>()
    private val completionMap = ConcurrentHashMap<String, Boolean>()

    fun handleTaskCompleted(identifier: String, costMillis: Long) {
        DebugLog.d(TAG, "handleTaskComplete: $identifier")
        completionMap[identifier] = true
        listeners.forEach {
            it.onTaskComplete(identifier, costMillis)
        }
    }

    fun isTaskComplete(identifier: String): Boolean {
        return completionMap[identifier] == true
    }

    fun addListener(listener: Listener) {
        listeners.add(listener)
    }

    fun removeListener(listener: Listener) {
        listeners.remove(listener)
    }

    interface Listener {
        fun onTaskComplete(identifier: String, costMillis: Long)
    }
}