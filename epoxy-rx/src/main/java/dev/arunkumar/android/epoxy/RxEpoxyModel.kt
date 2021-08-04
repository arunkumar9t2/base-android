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