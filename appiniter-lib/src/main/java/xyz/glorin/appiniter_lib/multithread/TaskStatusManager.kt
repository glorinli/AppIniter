package xyz.glorin.appiniter_lib.multithread

import xyz.glorin.appiniter_lib.DebugLog
import xyz.glorin.appiniter_lib.InitTask
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

    fun allComplete(tasks: List<InitTask>): Boolean {
        tasks.forEach {
            if (!isTaskComplete(it.identifier)) {
                return false
            }
        }

        return true
    }

    fun allDependenciesComplete(task: InitTask): Boolean {
        if (task.dependencies?.isNotEmpty() != true) {
            return true
        }

        task.dependencies?.forEach {
            if (!isTaskComplete(it)) {
                return false
            }
        }

        return true
    }

    interface Listener {
        fun onTaskComplete(identifier: String, costMillis: Long)
    }
}