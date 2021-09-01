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

@file:Suppress("NOTHING_TO_INLINE")

package dev.arunkumar.android.util

import androidx.compose.runtime.Composable
import dev.arunkumar.common.result.Resource
import dev.arunkumar.common.result.idle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

fun <T> Flow<T>.asResource(
  initial: Resource<T> = idle()
): Flow<Resource<T>> {
  return map<T, Resource<T>> { data -> Resource.Success(data) }
    .catch { throwable -> emit(Resource.Error(initial.unsafeValue, throwable)) }
    .onStart { emit(Resource.Loading(initial.unsafeValue)) }
}

@Composable
operator fun <T> Resource<T>.invoke(
  loading: @Composable (Resource.Loading<T>.() -> Unit)? = null,
  success: @Composable (Resource.Success<T>.() -> Unit)? = null,
  error: @Composable (Resource.Error<T>.() -> Unit)? = null,
  idle: @Composable (Resource.Idle<T>.() -> Unit)? = null
) {
  when (this) {
    is Resource.Success -> success?.invoke(this)
    is Resource.Loading -> loading?.invoke(this)
    is Resource.Error -> error?.invoke(this)
    is Resource.Idle -> idle?.invoke(this)
  }
}
