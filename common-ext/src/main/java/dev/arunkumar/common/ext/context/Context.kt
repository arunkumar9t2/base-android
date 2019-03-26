@file:Suppress("NOTHING_TO_INLINE")

package dev.arunkumar.common.ext.context

import android.content.Context
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat

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