package xyz.glorin.appiniter

import xyz.glorin.appiniter_lib.InitTask

class FrescoTask : InitTask() {
    override val identifier: String
        get() = "fresco"
    override val dependencies: List<String>?
        get() = null

    override fun run() {
        Thread.sleep(200L)
    }

}