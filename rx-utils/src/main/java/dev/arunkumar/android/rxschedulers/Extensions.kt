@file:Suppress("NOTHING_TO_INLINE")

package dev.arunkumar.android.rxschedulers

import android.os.Handler
import dev.arunkumar.common.os.HandlerExecutor
import io.reactivex.schedulers.Schedulers

inline fun Handler.toScheduler() = Schedulers.from(HandlerExecutor(this))