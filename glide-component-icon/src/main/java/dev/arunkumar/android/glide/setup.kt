package dev.arunkumar.android.glide

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import dev.arunkumar.android.glide.componenticon.ComponentIcon
import dev.arunkumar.android.glide.componenticon.ComponentIconDecoder
import dev.arunkumar.android.glide.componenticon.ComponentIconModelLoader

fun Registry.registerComponentIconLoader(context: Context, glide: Glide) {
  prepend(Uri::class.java, ComponentIcon::class.java, ComponentIconModelLoader.Factory())
  append(ComponentIcon::class.java, Bitmap::class.java, ComponentIconDecoder(context, glide))
}