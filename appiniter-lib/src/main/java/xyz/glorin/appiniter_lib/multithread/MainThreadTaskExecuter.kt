package xyz.glorin.appiniter_lib.multithread

class MainThreadTaskExecuter : AbstractTaskExecuter() {
    override fun start() {
        super.start()
        while (!allComplete()) {

        }
    }
}