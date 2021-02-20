package xyz.glorin.appiniter_lib.multithread

interface TaskCompleteListener {
    fun onTaskComplete(identifier: String, costMillis: Long)
}