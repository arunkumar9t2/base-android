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

package dev.arunkumar.common.recyclerview

import androidx.annotation.LayoutRes
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.arunkumar.common.context.dpToPx

fun GridCalculator.applyTo(recyclerView: RecyclerView) {
  gridData().let { (column, itemSpacing) ->
    recyclerView.apply {
      layoutManager = GridLayoutManager(context, column)
      updatePadding(left = itemSpacing, right = itemSpacing, top = itemSpacing / 2)
    }
  }
}

fun RecyclerView.autoGridSpacing(
  @LayoutRes referenceViewId: Int,
  parentWidth: () -> Int = { context.resources.displayMetrics.widthPixels },
  minimumSpacing: Int = context.dpToPx(2.0)
) = GridCalculator(context, referenceViewId, parentWidth, minimumSpacing).applyTo(this)