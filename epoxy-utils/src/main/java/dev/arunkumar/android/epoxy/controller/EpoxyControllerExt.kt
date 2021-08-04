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