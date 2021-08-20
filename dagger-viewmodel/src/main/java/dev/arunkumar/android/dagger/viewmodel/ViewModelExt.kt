/*
 * Copyright 2021 Arunkumar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.arunkumar.android.dagger.viewmodel

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
  ViewModelProvider(this, viewModelFactory()).get<T>()
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
  ViewModelProvider(this, viewModelFactory()).get<T>()
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
