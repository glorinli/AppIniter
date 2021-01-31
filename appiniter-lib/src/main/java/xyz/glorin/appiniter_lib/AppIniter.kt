package xyz.glorin.appiniter_lib

interface AppIniter {
    var listener: AppIniter.Listener?
    fun addTask(task: InitTask)
    fun run()

    companion object {
        private val initer = DefaultAppIniter()
        fun get() = initer
    }

    interface Listener {
        fun onStartExecute()
        fun onEndExecute(costMillis: Long)
    }
}