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

package dev.arunkumar.common.context

import android.content.Context
import android.util.TypedValue
import android.util.TypedValue.COMPLEX_UNIT_SP
import android.util.TypedValue.applyDimension
import androidx.annotation.AttrRes
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

inline fun Context.resolveColorAttribute(@AttrRes id: Int): Int {
  val typedValue = TypedValue()
  val typedArray = obtainStyledAttributes(typedValue.data, intArrayOf(id))
  return typedArray.getColor(0, 0).also { typedArray.recycle() }
}