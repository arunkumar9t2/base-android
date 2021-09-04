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

package dev.arunkumar.android.realm.threading

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.asCoroutineDispatcher
import kotlin.coroutines.CoroutineContext

class RealmDispatcher(
  private val tag: String? = null,
) : CoroutineDispatcher() {
  private val realmExecutor by lazy { RealmExecutor(tag) }
  private val delegateDispatcher by lazy { realmExecutor.asCoroutineDispatcher() }

  override fun dispatch(
    context: CoroutineContext,
    block: Runnable
  ) = delegateDispatcher.dispatch(context, block)

  override fun isDispatchNeeded(
    context: CoroutineContext
  ) = delegateDispatcher.isDispatchNeeded(context)
}

