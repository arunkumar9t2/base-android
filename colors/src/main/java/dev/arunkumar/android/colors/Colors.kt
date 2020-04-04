package dev.arunkumar.android.colors

import android.graphics.Color.parseColor
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils
import java.util.*

val DARK_VIBRANT_COLOR_700 = intArrayOf(
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

private fun colorDifference(@ColorInt a: Int, @ColorInt b: Int): Double {
  val aLab = DoubleArray(3)
  val bLab = DoubleArray(3)
  ColorUtils.colorToLAB(a, aLab)
  ColorUtils.colorToLAB(b, bLab)
  return ColorUtils.distanceEuclidean(aLab, bLab)
}

@ColorInt
fun closestVibrantDarkColor(@ColorInt color: Int): Int {
  val set: SortedMap<Double, Int> = TreeMap()
  for (i in DARK_VIBRANT_COLOR_700.indices) {
    set[colorDifference(color, DARK_VIBRANT_COLOR_700[i])] = i
  }
  return DARK_VIBRANT_COLOR_700[set[set.firstKey()]!!]
}