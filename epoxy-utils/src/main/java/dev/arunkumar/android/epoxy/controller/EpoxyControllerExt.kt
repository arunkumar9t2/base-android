package dev.arunkumar.android.epoxy.controller

import com.airbnb.epoxy.AsyncEpoxyController
import com.airbnb.epoxy.EpoxyController
import kotlin.properties.ObservableProperty
import kotlin.reflect.KProperty

/**
 * Property delegate that automatically calls [AsyncEpoxyController.requestDelayedModelBuild] when
 * the property changes
 */
fun <T> EpoxyController.model(initialValue: T) = object : ObservableProperty<T>(initialValue) {
  override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) {
    requestDelayedModelBuild(0)
  }
}

/**
 * Property delegate that automatically calls [AsyncEpoxyController.requestDelayedModelBuild] when
 * the property changes
 */
fun <T> EpoxyController.listModel(initialValue: List<T> = emptyList()) =
  object : ObservableProperty<List<T>>(initialValue) {
    override fun afterChange(property: KProperty<*>, oldValue: List<T>, newValue: List<T>) {
      requestDelayedModelBuild(0)
    }
  }