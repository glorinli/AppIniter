package xyz.glorin.appiniter_lib.multithread

import androidx.annotation.CallSuper
import xyz.glorin.appiniter_lib.DebugLog
import xyz.glorin.appiniter_lib.InitTask
import java.util.concurrent.LinkedBlockingDeque

abstract class AbstractTaskExecuter : TaskExecuter {
    private val tasks = mutableListOf<InitTask>()
    private val waitingTasks = mutableListOf<InitTask>()
    private val tasksToExecute = LinkedBlockingDeque<InitTask>()

    override fun addTask(task: InitTask) {
        tasks.add(task)
    }

    @CallSuper
    override fun start() {
        // Offer available tasks
        TaskDispatcher.runTask {
            waitingTasks.addAll(tasks)
        }
        scheduleExecutableTasks()
    }

    private fun scheduleExecutableTasks() {
        TaskDispatcher.runTask {
            waitingTasks.iterator().run {
                while (hasNext()) {
                    val task = next()
                    val allDependenciesComplete = allDependenciesComplete(task)
                    DebugLog.d(TAG, "allDep complete of ${task.identifier}: $allDependenciesComplete")
                    if (allDependenciesComplete) {
                        tasksToExecute.offer(task)
                        DebugLog.d(TAG, "offer task: ${task.identifier}")
                        remove()
                    }
                }
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

    companion object {
        private const val TAG = "AbstractTaskExecuter"
    }
}