package xyz.glorin.appiniter_lib.multithread

import xyz.glorin.appiniter_lib.InitTask

interface TaskExecuter {
    fun start()
    fun runTask(task: InitTask)
}