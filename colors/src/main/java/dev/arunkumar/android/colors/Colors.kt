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

package dev.arunkumar.android.colors

import android.graphics.Color.parseColor
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils

val DARK_VIBRANT_COLOR_700 by lazy {
  intArrayOf(
    parseColor("#D32F2F"),
    parseColor("#C2185B"),
    parseColor("#7B1FA2"),
    parseColor("#512DA8"),
    parseColor("#303F9F"),
    parseColor("#1976D2"),
    parseColor("#0288D1"),
    parseColor("#0097A7"),
    parseColor("#00796B"),
    parseColor("#388E3C"),
    parseColor("#F57F17"),
    parseColor("#E65100"),
    parseColor("#E64A19"),
    parseColor("#5D4037"),
    parseColor("#616161"),
    parseColor("#455A64")
  )
}

val VIBRANT_COLOR_500 by lazy {
  intArrayOf(
    parseColor("#F44336"),
    parseColor("#E91E63"),
    parseColor("#9C27B0"),
    parseColor("#673AB7"),
    parseColor("#3F51B5"),
    parseColor("#2196F3"),
    parseColor("#5E35B1"),
    parseColor("#3949AB"),
    parseColor("#1E88E5"),
    parseColor("#03A9F4"),
    parseColor("#00BCD4"),
    parseColor("#009688"),
    parseColor("#4CAF50"),
    parseColor("#8BC34A"),
    parseColor("#CDDC39"),
    parseColor("#FFEB3B"),
    parseColor("#FFC107"),
    parseColor("#FF9800"),
    parseColor("#FF5722"),
    parseColor("#795548"),
    parseColor("#9E9E9E")
  )
}

private fun colorDifference(@ColorInt a: Int, @ColorInt b: Int): Double {
  val aLab = DoubleArray(3)
  val bLab = DoubleArray(3)
  ColorUtils.colorToLAB(a, aLab)
  ColorUtils.colorToLAB(b, bLab)
  return ColorUtils.distanceEuclidean(aLab, bLab)
}

/**
 * Given an array of color ints, will return the color from the array that has shortest euclidean
 * distance with the given [color]
 */
@ColorInt
fun IntArray.closestColor(@ColorInt color: Int): Int =
  map { arrayColor -> arrayColor to colorDifference(arrayColor, color) }
    .sortedBy { it.second }
    .map { it.first }
    .first()

@ColorInt
fun closestVibrantDarkColor(@ColorInt color: Int): Int = DARK_VIBRANT_COLOR_700.closestColor(color)

@ColorInt
fun closestVibrantColor(@ColorInt color: Int): Int = VIBRANT_COLOR_500.closestColor(color)