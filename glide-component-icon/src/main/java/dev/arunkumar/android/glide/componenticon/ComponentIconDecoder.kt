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