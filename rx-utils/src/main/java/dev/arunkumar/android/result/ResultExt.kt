package dev.arunkumar.android.result

import dev.arunkumar.common.result.Result
import dev.arunkumar.common.result.idle
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

@Suppress("NOTHING_TO_INLINE")
inline fun <T> Observable<T>.asResult(initialResult: Result<T> = idle()) = compose { upstream ->
  upstream.map<Result<T>> { Result.Success(it) }
    .onErrorReturn { Result.Error(initialResult.unsafeValue, it) }
    .startWith(Result.Loading(initialResult.unsafeValue))
}

@Suppress("NOTHING_TO_INLINE")
inline fun <T> Flowable<T>.asResult(initialResult: Result<T> = idle()) = compose { upstream ->
  upstream.map<Result<T>> { Result.Success(it) }
    .onErrorReturn { Result.Error(initialResult.unsafeValue, it) }
    .startWith(Result.Loading(initialResult.unsafeValue))
}

@Suppress("NOTHING_TO_INLINE")
inline fun <T> Single<T>.asResult(initialResult: Result<T> = idle()) =
  toObservable().asResult(initialResult)