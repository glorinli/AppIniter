package xyz.glorin.appiniter_lib

import xyz.glorin.appiniter_lib.simple.SimpleAppIniter

interface AppIniter {
    var listener: Listener?
    fun addTask(task: InitTask)
    fun run()

    companion object {
        private val initer = SimpleAppIniter()
        fun get() = initer
    }

    interface Listener {
        fun onStartExecute()
        fun onEndExecute(costMillis: Long)
        fun onTaskComplete(identifier: String, costMillis: Long)
    }
}