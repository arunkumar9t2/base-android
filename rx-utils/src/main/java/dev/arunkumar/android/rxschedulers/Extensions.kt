@file:Suppress("NOTHING_TO_INLINE")

package dev.arunkumar.android.rxschedulers

import android.os.Handler
import dev.arunkumar.common.os.HandlerExecutor
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executor

inline fun Handler.toScheduler() = Schedulers.from(HandlerExecutor(this))

inline fun Executor.toScheduler() = Schedulers.from(this)