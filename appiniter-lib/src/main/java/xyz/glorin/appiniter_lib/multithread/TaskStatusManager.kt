package xyz.glorin.appiniter_lib.multithread

import java.util.concurrent.ConcurrentHashMap

object TaskStatusManager {
    private val listeners = mutableListOf<Listener>()
    private val completionMap = ConcurrentHashMap<String, Boolean>()

    fun handleTaskCompleted(identifier: String) {
        completionMap[identifier] = true
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
        fun onTaskComplete(identifier: String)
    }
}