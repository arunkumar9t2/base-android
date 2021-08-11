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

package dev.arunkumar.android.home.items

import android.app.Activity
import com.airbnb.epoxy.EpoxyAsyncUtil.getAsyncBackgroundHandler
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.jakewharton.rxrelay2.PublishRelay
import dev.arunkumar.android.dagger.activity.PerActivity
import dev.arunkumar.android.epoxy.controller.model
import dev.arunkumar.android.home.HomeAction
import dev.arunkumar.android.item.Item
import io.reactivex.Observable
import javax.inject.Inject

@PerActivity
class ItemsPagingController
@Inject
constructor(
  private val activity: Activity,
) : PagedListEpoxyController<Item>(
  modelBuildingHandler = getAsyncBackgroundHandler(),
  diffingHandler = getAsyncBackgroundHandler()
) {

  private val itemClicksRelay = PublishRelay.create<Item>()
  val itemClicks: Observable<Item> = itemClicksRelay.hide()

  private val resetDbRelay = PublishRelay.create<HomeAction.ResetItems>()
  val resetDbs: Observable<HomeAction.ResetItems> = resetDbRelay.hide()

  var headers by model(true)

  override fun addModels(models: List<EpoxyModel<*>>) {
    resetDbButton {
      id("reset-item-db")
      onClick { _ -> resetDbRelay.accept(HomeAction.ResetItems) }
    }

    if (headers && models.isNotEmpty()) {
      itemView {
        val header = (models.first() as ItemViewModel_).getText(activity).first().toString()
        id(header)
        text(header)
      }
    }

    models.forEachIndexed { index, currEpoxyModel ->

      currEpoxyModel.addTo(this)

      if (index != models.size - 1) {
        val startingChar = (currEpoxyModel as ItemViewModel_).getText(activity).first()
        val nextEpoxyModel = models[index + 1]
        val nextChar = (nextEpoxyModel as ItemViewModel_).getText(activity).first()
        if (!startingChar.equals(nextChar, ignoreCase = true)) {
          if (headers) {
            itemView {
              val header = nextChar.toString()
              id(header)
              text(header)
            }
          }
        }
      }
    }
  }

  override fun buildItemModel(
    currentPosition: Int,
    item: Item?
  ): EpoxyModel<*> {
    return if (item == null) {
      ItemViewModel_()
        .id("$currentPosition:null")
        .text("Loading..")
    } else ItemViewModel_()
      .id(item.id)
      .text(item.name)
      .onClick { _ -> itemClicksRelay.accept(item) }
  }
}
