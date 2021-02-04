package xyz.glorin.appiniter_lib

abstract class AbstractAppIniter : AppIniter {
    override var listener: AppIniter.Listener? = null

    protected val tasks = mutableListOf<InitTask>()

    override fun addTask(task: InitTask) {
        tasks.add(task)
    }
}