package dev.arunkumar.common.result

sealed class Result<T> {
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

    class Idle<T> : Result<T>()
    data class Loading<T>(val initialValue: T? = null) : Result<T>()
    data class Success<T>(val data: T) : Result<T>()
    data class Error<T>(val initialValue: T? = null, val throwable: Throwable) : Result<T>()
}

fun <T> idle() = Result.Idle<T>()