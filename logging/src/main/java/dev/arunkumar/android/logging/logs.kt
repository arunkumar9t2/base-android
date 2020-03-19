@file:Suppress("NOTHING_TO_INLINE")

package dev.arunkumar.android.logging

import android.app.Application
import android.util.Log
import dev.arunkumar.android.logging.BuildConfig.DEBUG_BUILD
import timber.log.Timber

/**
 * Includes given block of code in [action] only on debug builds.
 *
 * @param include to indicate if the block should be
 */
inline fun onDebug(include: Boolean = DEBUG_BUILD, action: () -> Unit) {
  if (include) {
    action()
  }
}

/**
 * Logs with [Log.DEBUG] level when [include] is `true`
 */
inline fun logd(
  message: String,
  include: Boolean = DEBUG_BUILD,
  throwable: Throwable? = null,
  vararg args: Any
) {
  if (include) {
    throwable?.let { Timber.d(it, message, args) } ?: Timber.d(message, args)
  }
}

/**
 * Logs with [Log.VERBOSE] level when [include] is `true`
 */
inline fun logv(
  message: String,
  include: Boolean = DEBUG_BUILD,
  throwable: Throwable? = null,
  vararg args: Any
) {
  if (include) {
    throwable?.let { Timber.v(it, message, args) } ?: Timber.v(message, args)
  }
}

/**
 * Logs with [Log.INFO] level when [include] is `true`
 */
inline fun logi(
  message: String,
  include: Boolean = DEBUG_BUILD,
  throwable: Throwable? = null,
  vararg args: Any
) {
  if (include) {
    throwable?.let { Timber.i(it, message, args) } ?: Timber.i(message, args)
  }
}

/**
 * Logs with [Log.INFO] level when [include] is `true`
 */
inline fun logw(
  message: String,
  include: Boolean = DEBUG_BUILD,
  throwable: Throwable? = null,
  vararg args: Any
) {
  if (include) {
    throwable?.let { Timber.w(it, message, args) } ?: Timber.w(message, args)
  }
}


/**
 * Logs with [Log.INFO] level when [include] is `true`
 */
inline fun loge(
  message: String,
  include: Boolean = DEBUG_BUILD,
  throwable: Throwable? = null,
  vararg args: Any
) {
  if (include) {
    throwable?.let { Timber.e(it, message, args) } ?: Timber.e(message, args)
  }
}

/**
 * Logs the given [throwable] with no message attached.
 */
inline fun logThrowable(throwable: Throwable) = loge(message = "", throwable = throwable)

/**
 * Initializes underlying logging framework. Typically this extension method should be called in [Application.onCreate]
 */
inline fun Application.initDebugLogs() {
  @Suppress("ConstantConditionIf")
  if (BuildConfig.DEBUG_BUILD) {
    Timber.plant(Timber.DebugTree())
  }
}