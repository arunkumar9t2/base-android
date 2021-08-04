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
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.data.DataFetcher

class ComponentIconDataFetcher(
  private val componentName: ComponentName
) : DataFetcher<ComponentIcon> {

  override fun loadData(
    priority: Priority,
    callback: DataFetcher.DataCallback<in ComponentIcon>
  ) {
    callback.onDataReady(ComponentIcon(componentName))
  }

  override fun cleanup() {}

  override fun cancel() {}

  override fun getDataClass(): Class<ComponentIcon> = ComponentIcon::class.java

  override fun getDataSource(): DataSource = DataSource.LOCAL
}