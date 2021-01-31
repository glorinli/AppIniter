package xyz.glorin.appiniter_lib

abstract class InitTask {
    abstract val identifier: String
    abstract val dependencies: List<String>?
    open val requireMainThread: Boolean = true
    abstract fun run()
}