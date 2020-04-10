package dev.arunkumar.android.preferences

import com.afollestad.rxkprefs.Pref

data class Preference<T>(
  val id: CharSequence,
  val title: CharSequence,
  val subTitle: CharSequence? = null
) {
  // Currently as lateint var because we don't want value to be considered during diff check
  lateinit var value: Pref<T>
}