@file:Suppress("NOTHING_TO_INLINE")

package dev.arunkumar.android.rx

import io.reactivex.Completable

inline fun completable(noinline action: () -> Unit): Completable = Completable.fromAction(action)