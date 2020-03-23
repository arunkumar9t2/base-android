package dev.arunkumar.android.preferences

import com.afollestad.rxkprefs.Pref

data class Preference<T>(
  val id: CharSequence,
  val title: CharSequence,
  val subTitle: CharSequence? = null
) {
  lateinit var value: Pref<T>
}