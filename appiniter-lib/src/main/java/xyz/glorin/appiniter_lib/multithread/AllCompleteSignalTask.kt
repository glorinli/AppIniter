package xyz.glorin.appiniter_lib.multithread

import xyz.glorin.appiniter_lib.InitTask

class AllCompleteSignalTask : InitTask() {
    override val identifier: String
        get() = ""
    override val dependencies: List<String>?
        get() = null

    override fun run() {
        // Noops
    }
}