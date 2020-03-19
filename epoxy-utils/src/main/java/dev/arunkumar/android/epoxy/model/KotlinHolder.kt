package dev.arunkumar.android.epoxy.model

import android.view.View
import com.airbnb.epoxy.EpoxyHolder
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.*

abstract class KotlinHolder : EpoxyHolder(), LayoutContainer {

  override lateinit var containerView: View

  override fun bindView(itemView: View) {
    containerView = itemView
  }

  fun unbindView() {
    clearFindViewByIdCache()
  }
}