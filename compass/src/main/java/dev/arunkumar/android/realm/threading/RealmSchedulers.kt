package dev.arunkumar.android.realm.threading

import dev.arunkumar.android.rxschedulers.toScheduler
import dev.arunkumar.android.transformers.CompositeTransformer
import io.reactivex.*

object RealmSchedulers {

  class RealmBgCompositeTransformer<T>(private val tag: String? = null) : CompositeTransformer<T> {

    private fun createRealmExecutor() = RealmExecutor(tag)

    override fun apply(upstream: Flowable<T>): Flowable<T> = createRealmExecutor().let {
      val scheduler = it.toScheduler()
      upstream.subscribeOn(scheduler)
        .doAfterTerminate { it.stop() }
        .unsubscribeOn(scheduler)
    }

    override fun apply(upstream: Single<T>): SingleSource<T> = createRealmExecutor().let {
      val scheduler = it.toScheduler()
      upstream.subscribeOn(scheduler)
        .doAfterTerminate { it.stop() }
        .unsubscribeOn(scheduler)
    }

    override fun apply(upstream: Completable): CompletableSource = createRealmExecutor().let {
      val scheduler = it.toScheduler()
      upstream.subscribeOn(scheduler)
        .doAfterTerminate { it.stop() }
        .unsubscribeOn(scheduler)
    }

    override fun apply(upstream: Observable<T>): ObservableSource<T> = createRealmExecutor().let {
      val scheduler = it.toScheduler()
      upstream.subscribeOn(scheduler)
        .doAfterTerminate { it.stop() }
        .unsubscribeOn(scheduler)
    }
  }

  fun <T> apply(tag: String? = null): CompositeTransformer<T> = RealmBgCompositeTransformer(tag)
}