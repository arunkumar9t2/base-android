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