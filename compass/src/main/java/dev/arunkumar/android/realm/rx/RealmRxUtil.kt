package dev.arunkumar.android.realm.rx

import dev.arunkumar.android.realm.threading.RealmExecutor
import dev.arunkumar.android.transformers.CompositeTransformer
import io.reactivex.*
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Publisher

fun <T> realmBgTransformer(): CompositeTransformer<T> {
  val realmExecutor = RealmExecutor()
  val realmScheduler = Schedulers.from(realmExecutor)
  val terminateAction: () -> Unit = { realmExecutor.stop() }
  return object : CompositeTransformer<T> {
    override fun apply(upstream: Flowable<T>): Publisher<T> {
      return upstream.subscribeOn(realmScheduler).doAfterTerminate(terminateAction)
    }

    override fun apply(upstream: Single<T>): SingleSource<T> {
      return upstream.subscribeOn(realmScheduler).doAfterTerminate(terminateAction)
    }

    override fun apply(upstream: Completable): CompletableSource {
      return upstream.subscribeOn(realmScheduler).doAfterTerminate(terminateAction)
    }

    override fun apply(upstream: Observable<T>): ObservableSource<T> {
      return upstream.subscribeOn(realmScheduler).doAfterTerminate(terminateAction)
    }
  }
}