package xyz.glorin.appiniter_lib.multithread

import androidx.annotation.CallSuper
import xyz.glorin.appiniter_lib.InitTask
import java.util.concurrent.LinkedBlockingDeque

abstract class AbstractTaskExecuter : TaskExecuter {
    private val tasks = mutableListOf<InitTask>()
    private val tasksToExecute = LinkedBlockingDeque<InitTask>()

    override fun addTask(task: InitTask) {
        tasks.add(task)
    }

    @CallSuper
    override fun start() {
        // Offer available tasks
        scheduleExecutableTasks()
    }

    private fun scheduleExecutableTasks() {
        tasks.forEach {
            if (!TaskStatusManager.isTaskComplete(it.identifier) && allDependenciesComplete(it)) {
                tasksToExecute.offer(it)
            }
        }
    }

    override fun notifyTaskComplete(identifier: String) {
        scheduleExecutableTasks()
    }

    fun allComplete(): Boolean {
        tasks.forEach {
            if (!TaskStatusManager.isTaskComplete(it.identifier)) {
                return false
            }
        }

        return true
    }

    fun nextExecutableTask(): InitTask? {
        return tasksToExecute.take()
    }

    private fun allDependenciesComplete(task: InitTask): Boolean {
        if (task.dependencies?.isNotEmpty() != true) {
            return true
        }

        task.dependencies?.forEach {
            if (!TaskStatusManager.isTaskComplete(it)) {
                return false
            }
        }

        return true
    }
}