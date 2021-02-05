package xyz.glorin.appiniter_lib

import java.lang.IllegalStateException

abstract class AbstractAppIniter : AppIniter {
    override var listener: AppIniter.Listener? = null

    protected val tasks = mutableListOf<InitTask>()
    private val taskIds = mutableSetOf<String>()
    private val dependedCountMap = mutableMapOf<String, Int>()

    override fun addTask(task: InitTask) {
        tasks.add(task)

        if (taskIds.contains(task.identifier)) {
            throw IllegalStateException("Already exists task with id: ${task.identifier}")
        } else {
            taskIds.add(task.identifier)
        }
    }

    final override fun run() {
        checkDependencies()
        sortTasks()
        onRun()
    }

    private fun checkDependencies() {
        tasks.forEach {
            it.dependencies?.forEach { d ->
                if (!taskIds.contains(d)) {
                    throw IllegalStateException("No task found for id: $d, required for task: ${it.identifier}")
                }

                dependedCountMap[d] = (dependedCountMap[d] ?: 0) + 1
            }
        }

        // TODO Check cycling dependencies
    }

    /**
     * Sort, more depended, run first
     */
    private fun sortTasks() {
        tasks.sortByDescending { dependedCountMap[it.identifier] }
    }

    abstract fun onRun()
}