@file:Suppress("NOTHING_TO_INLINE")

package dev.arunkumar.android.rx

import io.reactivex.*

/** Observable extensions **/
inline fun <T> deferObservable(noinline block: () -> Observable<T>): Observable<T> =
  Observable.defer(block)

inline fun <T> callableObservable(crossinline callableAction: () -> T): Observable<T> =
  Observable.fromCallable { callableAction() }

inline fun <T> createObservable(crossinline creator: ObservableEmitter<T>.() -> Unit): Observable<T> {
  return Observable.create { emitter -> creator(emitter) }
}

inline fun <T> observableConcat(vararg observables: Observable<T>): Observable<T> {
  return Observable.concatArray(*observables)
}

/** Observable extensions - end **/

/** Flowable extensions **/
inline fun <T> deferFlowable(noinline block: () -> Flowable<T>): Flowable<T> = Flowable.defer(block)

/** Flowable extensions - end **/


/** Completable extensions **/
inline fun completable(noinline action: () -> Unit): Completable = Completable.fromAction(action)

/** Completable extensions - end **/

/** Single extensions **/
inline fun <T> createSingle(crossinline creator: SingleEmitter<T>.() -> Unit): Single<T> {
  return Single.create { emitter -> creator(emitter) }
}

/** Single extensions - end **/