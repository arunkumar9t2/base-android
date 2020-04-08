@file:Suppress("NOTHING_TO_INLINE")

package dev.arunkumar.android.realm.epoxy

import com.airbnb.epoxy.EpoxyAsyncUtil
import dev.arunkumar.android.rxschedulers.toScheduler

inline fun epoxyBgScheduler() = EpoxyAsyncUtil.getAsyncBackgroundHandler().toScheduler()