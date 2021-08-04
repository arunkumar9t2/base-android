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

import android.content.ComponentName
import android.net.Uri
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoader.LoadData
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.signature.ObjectKey
import dev.arunkumar.android.glide.componenticon.ComponentIcon.Companion.URI_SCHEME_COMPONENT_ICON

class ComponentIconModelLoader : ModelLoader<Uri, ComponentIcon> {

  override fun buildLoadData(
    model: Uri,
    width: Int,
    height: Int,
    options: Options
  ) = ComponentName.unflattenFromString(model.schemeSpecificPart)?.let {
    LoadData(ObjectKey(model), ComponentIconDataFetcher(it))
  }

  override fun handles(model: Uri) = model.scheme == URI_SCHEME_COMPONENT_ICON

  class Factory : ModelLoaderFactory<Uri, ComponentIcon> {

    override fun build(multiFactory: MultiModelLoaderFactory) = ComponentIconModelLoader()

    override fun teardown() {}
  }
}