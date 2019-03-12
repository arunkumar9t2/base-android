@file:Suppress("NOTHING_TO_INLINE")

package dev.arunkumar.extensions.context

import android.content.Context
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat


fun Context.dpToPx(dp: Double): Int {
    val displayMetrics = resources.displayMetrics
    return ((dp * displayMetrics.density + 0.5).toInt())
}

fun Context.pxToDp(px: Int): Int {
    val displayMetrics = resources.displayMetrics
    return (px / displayMetrics.density).toInt()
}

@ColorInt
inline fun Context.resolveColorRes(@ColorRes id: Int): Int {
    return ContextCompat.getColor(this, id)
}