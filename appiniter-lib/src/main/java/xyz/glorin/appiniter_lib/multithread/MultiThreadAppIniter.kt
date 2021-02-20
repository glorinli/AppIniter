package xyz.glorin.appiniter_lib.multithread

import android.os.SystemClock
import xyz.glorin.appiniter_lib.AbstractAppIniter
import xyz.glorin.appiniter_lib.DebugLog
import xyz.glorin.appiniter_lib.InitTask
import xyz.glorin.appiniter_lib.multithread.executers.MainThreadTaskExecuter
import xyz.glorin.appiniter_lib.multithread.executers.ThreadedTaskExecuter
import java.util.concurrent.ConcurrentHashMap

class MultiThreadAppIniter : AbstractAppIniter(), TaskCompleteListener {
    private val mainThreadExecuter = MainThreadTaskExecuter(this)
    private val threadedTaskExecuter = ThreadedTaskExecuter(this)
    private val taskDispatcher = TaskDispatcher()
    private val waitingTasks = mutableListOf<InitTask>()
    private val completionMap = ConcurrentHashMap<String, Boolean>()

    override fun onRun() {
        listener?.onStartExecute()
        val start = SystemClock.uptimeMillis()

        if (tasks.isEmpty()) {
            return
        }

        waitingTasks.addAll(tasks)

        taskDispatcher.start()
        dispatchTask()

        threadedTaskExecuter.start()
        mainThreadExecuter.start()

        listener?.onEndExecute(SystemClock.uptimeMillis() - start)

        taskDispatcher.quitSafely()
    }

    private fun dispatchTask() {
        taskDispatcher.runTask(Runnable{
            waitingTasks.iterator().run {
                while (hasNext()) {
                    val task = next()
                    val allDependenciesComplete = allDependenciesComplete(task)
                    DebugLog.d(TAG, "allDep complete of ${task.identifier}: $allDependenciesComplete")
                    if (allDependenciesComplete) {
                        DebugLog.d(TAG, "offer task: ${task.identifier}")
                        if (task.requireMainThread) {
                            mainThreadExecuter.runTask(task)
                        } else {
                            threadedTaskExecuter.runTask(task)
                        }
                        remove()
                    }
                }
            }
        })
    }

    private fun allDependenciesComplete(task: InitTask): Boolean {
        if (task.dependencies?.isNotEmpty() != true) {
            return true
        }

        task.dependencies?.forEach {
            if (!isTaskComplete(it)) {
                return false
            }
        }

        return true
    }

    private fun isTaskComplete(identifier: String): Boolean {
        return completionMap[identifier] == true
    }

    private fun allComplete(tasks: List<InitTask>): Boolean {
        tasks.forEach {
            if (!isTaskComplete(it.identifier)) {
                return false
            }
        }

        return true
    }

    override fun onTaskComplete(identifier: String, costMillis: Long) {
        taskDispatcher.runTask(Runnable {
            DebugLog.d(TAG, "handleTaskComplete: $identifier")
            completionMap[identifier] = true

            listener?.onTaskComplete(identifier, costMillis)

            if (allComplete(tasks)) {
                mainThreadExecuter.runTask(AllCompleteSignalTask())
            } else {
                dispatchTask()
            }
        })
    }

    companion object {
        private const val TAG = "MultiThreadAppIniter"
    }
}