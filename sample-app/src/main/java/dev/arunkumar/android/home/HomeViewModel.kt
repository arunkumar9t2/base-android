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

package dev.arunkumar.android.home

import com.afollestad.rxkprefs.RxkPrefs
import com.afollestad.rxkprefs.rxjava.observe
import com.babylon.orbit.LifecycleAction
import com.babylon.orbit.OrbitViewModel
import dev.arunkumar.android.epoxy.epoxyAsyncScheduler
import dev.arunkumar.android.item.Item
import dev.arunkumar.android.item.ItemsRepository
import dev.arunkumar.android.item.ResetItems
import dev.arunkumar.android.preferences.Preference
import dev.arunkumar.android.result.asResource
import io.realm.kotlin.where
import javax.inject.Inject

class HomeViewModel
@Inject
constructor(
  private val rxkPrefs: RxkPrefs,
  private val itemsRepository: ItemsRepository,
  private val resetItems: ResetItems
) : OrbitViewModel<HomeState, HomeSideEffect>(HomeState(), {

  val sortPreference: Preference<Boolean> by lazy {
    val sortPreferenceId = "sortPreference"
    Preference<Boolean>(
      sortPreferenceId,
      "Sort items"
    ).apply {
      value = rxkPrefs.boolean(sortPreferenceId, true)
    }
  }

  val headerPreference: Preference<Boolean> by lazy {
    val headerPreferenceId = "headerPreference"
    Preference<Boolean>(
      headerPreferenceId,
      "Show headers"
    ).apply {
      value = rxkPrefs.boolean(headerPreferenceId, false)
    }
  }

  val preferences: List<Preference<*>> by lazy { listOf(sortPreference, headerPreference) }

  perform("load items")
    .on<LifecycleAction.Created>()
    .reduce { currentState.copy(preferences = preferences) }
    .transform {
      eventObservable.flatMap {
        sortPreference.value.observe()
          .switchMap { sort ->
            itemsRepository.pagedItems<Item>(notifyScheduler = epoxyAsyncScheduler()) { realm ->
              realm.where<Item>().let { if (sort) it.sort("name") else it }
            }.asResource()
          }
      }
    }.reduce { currentState.copy(items = event) }

  perform("observe header preference")
    .on<LifecycleAction.Created>()
    .transform { eventObservable.flatMap { headerPreference.value.observe() } }
    .reduce { currentState.copy(headers = event) }

  perform("delete item")
    .on<HomeAction.DeleteItem>()
    .sideEffect { post(HomeSideEffect.PerformDelete(event.item)) }

  perform("reset db")
    .on<HomeAction.ResetItems>()
    .transform { eventObservable.flatMap { resetItems.build().toObservable<Any>() } }
})
