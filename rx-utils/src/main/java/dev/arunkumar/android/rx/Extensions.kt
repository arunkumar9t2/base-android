@file:Suppress("NOTHING_TO_INLINE")

package dev.arunkumar.android.rx

import io.reactivex.Completable
import io.reactivex.Observable

/** Observable extensions **/
inline fun <T> deferObservable(noinline block: () -> Observable<T>): Observable<T> = Observable.defer(block)

inline fun completable(noinline action: () -> Unit): Completable = Completable.fromAction(action)