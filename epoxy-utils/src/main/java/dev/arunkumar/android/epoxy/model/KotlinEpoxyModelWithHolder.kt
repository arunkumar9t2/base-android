package dev.arunkumar.android.epoxy.model

import com.airbnb.epoxy.EpoxyModelWithHolder

abstract class KotlinEpoxyModelWithHolder<T : KotlinHolder> : EpoxyModelWithHolder<T>() {

  override fun unbind(holder: T) {
    super.unbind(holder)
    holder.unbindView()
  }
}