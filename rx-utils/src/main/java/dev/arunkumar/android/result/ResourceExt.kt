package dev.arunkumar.android.result

import dev.arunkumar.common.result.Resource
import dev.arunkumar.common.result.idle
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

@Suppress("NOTHING_TO_INLINE")
inline fun <T> Observable<T>.asResource(initialResource: Resource<T> = idle()) =
  compose { upstream ->
    upstream.map<Resource<T>> { Resource.Success(it) }
      .onErrorReturn { Resource.Error(initialResource.unsafeValue, it) }
      .startWith(Resource.Loading(initialResource.unsafeValue))
  }

@Suppress("NOTHING_TO_INLINE")
inline fun <T> Flowable<T>.asResource(initialResource: Resource<T> = idle()) = compose { upstream ->
  upstream.map<Resource<T>> { Resource.Success(it) }
    .onErrorReturn { Resource.Error(initialResource.unsafeValue, it) }
    .startWith(Resource.Loading(initialResource.unsafeValue))
}

@Suppress("NOTHING_TO_INLINE")
inline fun <T> Single<T>.asResource(initialResource: Resource<T> = idle()) =
  toObservable().asResource(initialResource)