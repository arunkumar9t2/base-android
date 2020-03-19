@file:Suppress("NOTHING_TO_INLINE")

package dev.arunkumar.common.context

import android.content.Context
import android.util.TypedValue.COMPLEX_UNIT_SP
import android.util.TypedValue.applyDimension
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat


fun Context.dpToPx(dp: Double): Int {
  val displayMetrics = resources.displayMetrics
  return ((dp * displayMetrics.density + 0.5).toInt())
}

fun Context.pxToDp(px: Int): Int {
  val displayMetrics = resources.displayMetrics
  return (px / displayMetrics.density).toInt()
}

fun Context.spToPx(sp: Float) = applyDimension(
  COMPLEX_UNIT_SP,
  sp,
  resources.displayMetrics
).toInt()

@ColorInt
inline fun Context.resolveColorRes(@ColorRes id: Int): Int = ContextCompat.getColor(this, id)