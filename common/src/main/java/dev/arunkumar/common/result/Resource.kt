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