package dev.arunkumar.android.glide.componenticon

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.ResourceDecoder
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapResource

class ComponentIconDecoder(
  private val context: Context,
  glide: Glide
) : ResourceDecoder<ComponentIcon, Bitmap> {
  private val bitmapPool: BitmapPool = glide.bitmapPool

  override fun handles(source: ComponentIcon, options: Options) = true

  override fun decode(source: ComponentIcon, with: Int, height: Int, options: Options) = try {
    context.packageManager.getActivityIcon(source.componentName).toBitmap().let { bitmap ->
      val bitmapCopy = bitmap.copy(bitmap.config, true)
      BitmapResource.obtain(bitmapCopy, bitmapPool)
    }
  } catch (e: PackageManager.NameNotFoundException) {
    null
  }
}