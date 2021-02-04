package xyz.glorin.appiniter_lib.multithread

import xyz.glorin.appiniter_lib.AbstractAppIniter

class MultiThreadAppIniter : AbstractAppIniter() {
    private val mainThreadExecuter = MainThreadTaskExecuter()
    private val threadPoolTaskExecuter = ThreadPoolTaskExecuter()

    override fun run() {
        if (tasks.isEmpty()) {
            return
        }

        tasks.forEach {
            if (it.requireMainThread) {
                mainThreadExecuter.addTask(it)
            } else {
                threadPoolTaskExecuter.addTask(it)
            }
        }

        threadPoolTaskExecuter.start()
        mainThreadExecuter.start()
    }
}