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