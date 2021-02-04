package xyz.glorin.appiniter_lib.multithread

import xyz.glorin.appiniter_lib.InitTask

interface TaskExecuter {
    fun addTask(task: InitTask)
    fun start()
    fun notifyTaskComplete(identifier: String)
}