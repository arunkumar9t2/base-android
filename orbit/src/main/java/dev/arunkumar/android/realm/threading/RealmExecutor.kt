package dev.arunkumar.android.realm.threading

import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.os.Process.THREAD_PRIORITY_BACKGROUND
import dev.arunkumar.common.os.HandlerExecutor
import java.util.concurrent.Executor

interface StoppableExecutor : Executor {
    fun stop()
}

class RealmExecutor(private val tag: String? = null) : StoppableExecutor {

    private val handlerThread by lazy {
        HandlerThread(
            tag ?: this::class.java.simpleName + hashCode(),
            THREAD_PRIORITY_BACKGROUND
        ).apply { start() }
    }
    private val handlerExecutor by lazy { HandlerExecutor(Handler(handlerThread.looper)) }

    override fun execute(command: Runnable) = handlerExecutor.execute(command)

    override fun stop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            handlerThread.quitSafely()
        } else {
            handlerThread.quit()
        }
    }
}