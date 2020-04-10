package dev.arunkumar.android.epoxy

import dev.arunkumar.android.epoxy.model.KotlinHolder

abstract class SimpleRxModel : RxEpoxyModel<SimpleRxModel.ViewHolder>() {
  class ViewHolder : KotlinHolder()
}