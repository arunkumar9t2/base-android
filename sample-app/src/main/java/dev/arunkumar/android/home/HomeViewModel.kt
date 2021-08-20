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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import dev.arunkumar.android.item.Item
import dev.arunkumar.android.item.ItemsRepository
import dev.arunkumar.android.preferences.Preference
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeState(
  val preferences: List<Preference<*>> = emptyList(),
  val headers: Boolean = true,
  val items: PagingData<Item> = PagingData.empty()
)

sealed class HomeSideEffect {
  data class PerformDelete(val item: Item) : HomeSideEffect()
}

sealed class HomeAction {
  object LoadItems : HomeAction()
  data class DeleteItem(val item: Item) : HomeAction()
  object ResetItems : HomeAction()
}

private typealias HomeReducer = HomeState.() -> HomeState

class HomeViewModel
@Inject
constructor(
  private val itemsRepository: ItemsRepository,
) : ViewModel() {
  /**
   * Action stream for processing set of UI actions received from View
   */
  private val actions = MutableSharedFlow<HomeAction>()
  private val actionsFlow = actions.asSharedFlow()

  // actions to reducers
  private val loadItemsReducer = onAction<HomeAction.LoadItems>()
    .flatMapLatest {
      itemsRepository.pagedItems<Item>(realmQueryBuilder = Realm::where)
    }.map<PagingData<Item>, HomeReducer> { items ->
      {
        copy(items = items, headers = false)
      }
    }

  /**
   * StateFlow should basically be a `StateFlow<HomeState>` produced by processing all reducers
   */
  val state = merge(loadItemsReducer)
    .scan(HomeState()) { state, reducer -> reducer(state) }
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = HomeState()
    ).asLiveData()

  // TODO Implement one off without caching but lifecycle aware like LiveData
  private val _effects = MutableSharedFlow<HomeSideEffect>()
  val effects = _effects.asSharedFlow()

  private inline fun <reified Action : HomeAction> onAction() = actionsFlow
    // TODO Figure out thread here, in RxJava we can observe using observeOn, but flow only has
    // flowOn
    .filterIsInstance<Action>()

  fun perform(action: HomeAction) {
    viewModelScope.launch {
      actions.emit(action)
    }
  }
}
