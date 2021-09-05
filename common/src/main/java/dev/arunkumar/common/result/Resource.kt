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

package dev.arunkumar.common.result

import dev.arunkumar.common.result.Resource.*

sealed class Resource<T> {

  val hasValue: Boolean
    get() = when (this) {
      is Success -> true
      is Loading -> this.initialValue != null
      is Error -> this.initialValue != null
      else -> false
    }

  val value: T
    get() = when (this) {
      is Success -> this.data
      is Loading -> this.initialValue ?: throw RuntimeException("No data present")
      is Error -> this.initialValue ?: throw RuntimeException("No data present")
      else -> throw RuntimeException("No data present")
    }

  val unsafeValue: T? get() = if (hasValue) value else null

  class Idle<T> : Resource<T>()
  data class Loading<T>(val initialValue: T? = null) : Resource<T>()
  data class Success<T>(val data: T) : Resource<T>()
  data class Error<T>(val initialValue: T? = null, val throwable: Throwable) : Resource<T>()
}

fun <T> idle() = Idle<T>()

fun <T> Resource<T>.on(
  loading: (Loading<T>.() -> Unit)? = null,
  success: (Success<T>.() -> Unit)? = null,
  error: (Error<T>.() -> Unit)? = null,
  idle: (Idle<T>.() -> Unit)? = null
) {
  when (this) {
    is Success -> success?.invoke(this)
    is Loading -> loading?.invoke(this)
    is Error -> error?.invoke(this)
    is Idle -> idle?.invoke(this)
  }
}

fun <T> Resource<T>.success(success: Success<T>.() -> Unit) {
  on(success = success)
}
