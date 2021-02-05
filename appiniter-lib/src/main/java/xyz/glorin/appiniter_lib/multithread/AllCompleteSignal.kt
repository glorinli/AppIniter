package xyz.glorin.appiniter_lib.multithread

import xyz.glorin.appiniter_lib.DebugLog
import java.util.concurrent.SynchronousQueue

class AllCompleteSignal {
    private val queue = SynchronousQueue<Int>()

    fun waitForAllComplete() {
        DebugLog.d(TAG, "waitForAllComplete")
        queue.take()
    }

    fun notifyAllComplete() {
        DebugLog.d(TAG, "notifyAllComplete")
        queue.offer(0)
    }

    companion object {
        private const val TAG = "AllCompleteSignal"
    }
}