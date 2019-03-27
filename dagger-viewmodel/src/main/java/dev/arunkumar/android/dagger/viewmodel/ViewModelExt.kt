package dev.arunkumar.android.dagger.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * [ViewModelProvider.Factory] implementation that uses [Class.newInstance] to instantiate ViewModels.
 */
private val NEW_INSTANCE_FACTORY = ViewModelProvider.NewInstanceFactory()

/**
 * Marker to denote that implementing class uses [ViewModel]
 */
interface UsesViewModel {
    /**
     * The [ViewModelProvider.Factory] that should be used to create [ViewModel] instances
     */
    val viewModelFactory: ViewModelProvider.Factory
}


/**
 * Shortcut to avoid passing ugly `class` objects
 */
inline fun <reified T : ViewModel> ViewModelProvider.get(): T = get(T::class.java)

/**
 * Returns custom [ViewModelProvider.Factory] instance provided via [UsesViewModel] if the [AppCompatActivity] implements
 * [UsesViewModel] else returns [ViewModelProvider.NewInstanceFactory]
 */
val AppCompatActivity.vmFactory
    get() = if (this is UsesViewModel) {
        viewModelFactory
    } else {
        NEW_INSTANCE_FACTORY
    }

/**
 * Creates [ViewModel] instance of type [T] by using the provided [viewModelFactory] and caches it future use. By
 * default, this method uses [ViewModelProvider.NewInstanceFactory] to instantiate [T] type.
 */
inline fun <reified T : ViewModel> AppCompatActivity.viewModel(
    crossinline viewModelFactory: () -> ViewModelProvider.Factory = { vmFactory }
) = FastLazy { _: AppCompatActivity, _: KProperty<*> ->
    ViewModelProviders.of(this, viewModelFactory()).get<T>()
}


/**
 * Returns custom [ViewModelProvider.Factory] instance provided via [UsesViewModel] if the [Fragment] implements [UsesViewModel]
 * else returns [ViewModelProvider.NewInstanceFactory]
 */
val Fragment.vmFactory
    get() = if (this is UsesViewModel) {
        viewModelFactory
    } else {
        NEW_INSTANCE_FACTORY
    }

/**
 * Creates [ViewModel] instance of type [T] by using the provided [viewModelFactory] and caches it future use. By
 * default, this method uses [ViewModelProvider.NewInstanceFactory] to instantiate [T] type.
 */
inline fun <reified T : ViewModel> Fragment.viewModel(
    crossinline viewModelFactory: () -> ViewModelProvider.Factory = { vmFactory }
) = FastLazy { _: AppCompatActivity, _: KProperty<*> ->
    ViewModelProviders.of(this, viewModelFactory()).get<T>()
}


/**
 * Similar to Kotlin's own [Lazy] delegate by takes the initialization logic as a lambda in constructor and use it in
 * [getValue].
 *
 * Note: There is no synchronization performed.
 */
class FastLazy<T, V>(private val initializer: (T, KProperty<*>) -> V) : ReadOnlyProperty<T, V> {
    private object EMPTY

    private var value: Any? = EMPTY

    override fun getValue(thisRef: T, property: KProperty<*>): V {
        if (value == EMPTY) {
            value = initializer(thisRef, property)
        }
        @Suppress("UNCHECKED_CAST")
        return value as V
    }
}