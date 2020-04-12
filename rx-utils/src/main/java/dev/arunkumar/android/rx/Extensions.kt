@file:Suppress("NOTHING_TO_INLINE")

package dev.arunkumar.android.rx

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.ObservableEmitter

/** Observable extensions **/
inline fun <T> deferObservable(noinline block: () -> Observable<T>): Observable<T> =
  Observable.defer(block)

inline fun <T> callableObservable(crossinline callableAction: () -> T): Observable<T> =
  Observable.fromCallable { callableAction() }

inline fun <T> createObservable(crossinline creator: ObservableEmitter<T>.() -> Unit): Observable<T> {
  return Observable.create { emitter -> creator(emitter) }
}

inline fun completable(noinline action: () -> Unit): Completable = Completable.fromAction(action)

inline fun <T> deferFlowable(noinline block: () -> Flowable<T>): Flowable<T> = Flowable.defer(block)