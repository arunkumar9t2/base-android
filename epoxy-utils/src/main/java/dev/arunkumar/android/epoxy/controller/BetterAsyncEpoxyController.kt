package dev.arunkumar.android.epoxy.controller

import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.AsyncEpoxyController
import kotlin.properties.ObservableProperty
import kotlin.reflect.KProperty

abstract class BetterAsyncEpoxyController : AsyncEpoxyController() {

    private var didCallModelBuild = false

    /**
     * Property delegate that automatically calls [requestModelBuild] when the property changes
     */
    fun <T> model(initialValue: T) = object : ObservableProperty<T>(initialValue) {
        override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) {
            requestModelBuild()
        }
    }

    /**
     * Hack to call [requestDelayedModelBuild] atleast once because by default it runs the first
     * model building in main thread. [onAttachedToRecyclerView] seems like a good place to call.
     * Could be moved else where after sufficient investigation.
     */
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        if (!didCallModelBuild) {
            requestDelayedModelBuild(0)
            didCallModelBuild = true
        }
    }
}