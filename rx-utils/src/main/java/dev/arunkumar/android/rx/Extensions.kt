@file:Suppress("NOTHING_TO_INLINE")

package dev.arunkumar.android.rx

import io.reactivex.Completable

inline fun completeable(noinline action: () -> Unit): Completable = Completable.fromAction(action)