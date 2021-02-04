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

class AppDTask : InitTask() {
    override val identifier: String
        get() = "appd"
    override val dependencies: List<String>?
        get() = null

    override fun run() {
        Thread.sleep(200L)
    }

}

class GlideTask : InitTask() {
    override val identifier: String
        get() = "glide"
    override val dependencies: List<String>?
        get() = null

    override fun run() {
        Thread.sleep(200L)
    }

}

class LogTask : InitTask() {
    override val identifier: String
        get() = "log"
    override val dependencies: List<String>?
        get() = null

    override fun run() {
        Thread.sleep(200L)
    }

}