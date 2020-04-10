package dev.arunkumar.android.epoxy

import dev.arunkumar.android.epoxy.model.KotlinEpoxyModelWithHolder
import dev.arunkumar.android.epoxy.model.KotlinHolder
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


/**
 * A Epoxy model that uses Kotlin android extensions for binding UI elements and also a `boundScope {}`
 * that
 */
abstract class RxEpoxyModel<T : KotlinHolder> : KotlinEpoxyModelWithHolder<T>() {

  protected val unBindDisposables = CompositeDisposable()

  protected inline fun RxEpoxyModel<T>.untilUnbind(block: () -> Disposable) {
    unBindDisposables.add(block())
  }

  override fun bind(holder: T) {
    super.bind(holder)
    unBindDisposables.clear()
  }

  override fun unbind(holder: T) {
    super.unbind(holder)
    unBindDisposables.clear()
  }
}