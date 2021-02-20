package xyz.glorin.appiniter_lib.multithread

import android.os.SystemClock
import xyz.glorin.appiniter_lib.AbstractAppIniter
import xyz.glorin.appiniter_lib.DebugLog
import xyz.glorin.appiniter_lib.InitTask

class MultiThreadAppIniter : AbstractAppIniter(), TaskStatusManager.Listener {
    private val mainThreadExecuter = MainThreadTaskExecuter()
    private val threadedTaskExecuter = ThreadedTaskExecuter()
    private val taskDispatcher = TaskDispatcher()
    private val waitingTasks = mutableListOf<InitTask>()

    override fun onRun() {
        listener?.onStartExecute()
        val start = SystemClock.uptimeMillis()

        if (tasks.isEmpty()) {
            return
        }

        waitingTasks.addAll(tasks)

        taskDispatcher.start()
        dispatchTask()

        TaskStatusManager.addListener(this)

        threadedTaskExecuter.start()
        mainThreadExecuter.start()

        listener?.onEndExecute(SystemClock.uptimeMillis() - start)

        taskDispatcher.quitSafely()
        TaskStatusManager.removeListener(this)
    }

    private fun dispatchTask() {
        taskDispatcher.runTask(Runnable{
            waitingTasks.iterator().run {
                while (hasNext()) {
                    val task = next()
                    val allDependenciesComplete = TaskStatusManager.allDependenciesComplete(task)
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

    override fun onTaskComplete(identifier: String, costMillis: Long) {
        listener?.onTaskComplete(identifier, costMillis)

        if (TaskStatusManager.allComplete(tasks)) {
            mainThreadExecuter.runTask(AllCompleteSignalTask())
        } else {
            dispatchTask()
        }
    }

    companion object {
        private const val TAG = "MultiThreadAppIniter"
    }
}