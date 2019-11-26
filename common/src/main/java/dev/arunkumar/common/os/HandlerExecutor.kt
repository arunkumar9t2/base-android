package dev.arunkumar.common.os

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor

class HandlerExecutor(private val handler: Handler) : Executor {

    override fun execute(command: Runnable) {
        if (Looper.myLooper() == handler.looper) {
            command.run()
        } else {
            handler.post(command)
        }
    }
}