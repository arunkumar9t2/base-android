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

package dev.arunkumar.android.epoxy

import com.airbnb.epoxy.EpoxyRecyclerView
import dev.arunkumar.common.recyclerview.GridCalculator
import dev.arunkumar.common.recyclerview.applyTo as baseApply

fun GridCalculator.applyTo(epoxyRecyclerView: EpoxyRecyclerView) {
  baseApply(epoxyRecyclerView)
  epoxyRecyclerView.setItemSpacingPx(gridData().itemSpacing)
}