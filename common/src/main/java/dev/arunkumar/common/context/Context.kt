@file:Suppress("NOTHING_TO_INLINE")

package dev.arunkumar.common.context

import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import dev.arunkumar.common.model.StringResource

/**
 * Converts given [dp] to pixel units
 */
fun Context.dpToPx(dp: Double): Int {
    val displayMetrics = resources.displayMetrics
    return ((dp * displayMetrics.density + 0.5).toInt())
}

/**
 * Converts given [px] pixels units to density independent units
 */
fun Context.pxToDp(px: Int): Int {
    val displayMetrics = resources.displayMetrics
    return (px / displayMetrics.density).toInt()
}

@ColorInt
inline fun Context.resolveColorRes(@ColorRes id: Int): Int = ContextCompat.getColor(this, id)

inline fun Context.resolveStringResource(
    stringResource: StringResource,
    args: Array<String> = emptyArray(),
    resourceArgs: Array<Int> = emptyArray()
) = when {
    args.isNotEmpty() -> getString(stringResource.resource, stringResource.args)
    resourceArgs.isNotEmpty() -> getString(stringResource.resource, resourceArgs.map(::getString))
    else -> getString(stringResource.resource)
}