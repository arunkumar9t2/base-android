package dev.arunkumar.android.colors

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.palette.graphics.Palette

fun Palette.pickDarkVibrantColor(): Palette.Swatch = when {
  darkVibrantSwatch != null -> darkVibrantSwatch!!
  else -> {
    val dominantSwatch = dominantSwatch!!
    Palette.Swatch(
      closestVibrantDarkColor(dominantSwatch.rgb),
      dominantSwatch.population
    )
  }
}

fun Bitmap.bestVibrantDarkColor(): Palette.Swatch {
  return Palette.from(this).generate().pickDarkVibrantColor()
}

fun BitmapDrawable.bestVibrantDarkColor(): Palette.Swatch {
  return bitmap.bestVibrantDarkColor()
}