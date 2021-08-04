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

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.palette.graphics.Palette
import androidx.palette.graphics.Palette.Swatch
import androidx.palette.graphics.Palette.from

fun Palette.pickDarkVibrantColor(): Swatch = when {
  darkVibrantSwatch != null -> darkVibrantSwatch!!
  else -> {
    val dominantSwatch = dominantSwatch!!
    Swatch(closestVibrantDarkColor(dominantSwatch.rgb), dominantSwatch.population)
  }
}

fun Bitmap.bestVibrantDarkColor(): Swatch {
  return from(this).generate().pickDarkVibrantColor()
}

fun BitmapDrawable.bestVibrantDarkColor(): Swatch {
  return bitmap.bestVibrantDarkColor()
}

fun Palette.pickVibrantColor(): Swatch = when {
  vibrantSwatch != null -> vibrantSwatch!!
  else -> {
    val dominantSwatch = dominantSwatch!!
    Swatch(closestVibrantColor(dominantSwatch.rgb), dominantSwatch.population)
  }
}

fun Bitmap.bestVibrantColor(): Swatch {
  return from(this).generate().pickVibrantColor()
}

fun BitmapDrawable.bestVibrantColor(): Swatch {
  return bitmap.bestVibrantColor()
}