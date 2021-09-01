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
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import dev.arunkumar.android.logging.logD
import dev.arunkumar.android.tasks.data.Task
import dev.arunkumar.android.tasks.data.TaskRepository
import dev.arunkumar.android.util.DispatcherProvider
import dev.arunkumar.android.util.asResource
import dev.arunkumar.android.util.printThread
import io.realm.kotlin.where
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.rx2.await
import javax.inject.Inject

data class HomeState(
  val tasks: Flow<PagingData<Task>> = flowOf(PagingData.empty())
)

sealed class HomeSideEffect

sealed class HomeAction {
  object LoadTasks : HomeAction()
  class AddTask(val taskName: String) : HomeAction()
}

private typealias HomeReducer = HomeState.() -> HomeState

class HomeViewModel
@Inject
constructor(
  dispatchers: DispatcherProvider,
  taskRepository: TaskRepository
) : ViewModel() {

  private val reducerDispatcher = newSingleThreadContext("Reducer")

  @Suppress("NOTHING_TO_INLINE")
  private inline fun Reducer(noinline reducer: HomeState.() -> HomeState) = reducer

  private val NoOp = Reducer { this }

  /**
   * Action stream for processing set of UI actions received from View
   */
  private val actions = MutableSharedFlow<HomeAction>()
  private val actionsFlow = actions.asSharedFlow()

  // TODO Implement one off without caching but lifecycle aware like LiveData
  private val _effects = MutableSharedFlow<HomeSideEffect>()
  val effects = _effects.asSharedFlow()

  // actions to reducers
  private val loadTasks: Flow<HomeReducer> = onAction<HomeAction.LoadTasks>()
    .onStart { emit(HomeAction.LoadTasks) }
    .map {
      taskRepository.addItemsIfEmpty().await()
      taskRepository.pagedItems<Task> {
        it.where()
      }
    }.flowOn(dispatchers.io)
    .map { pagedTasks ->
      Reducer {
        copy(tasks = pagedTasks)
      }
    }.shareIn(viewModelScope, SharingStarted.WhileSubscribed())

  private val addTask: Flow<HomeReducer> = onAction<HomeAction.AddTask>()
    .flatMapLatest { addTask ->
      flow {
        emit(taskRepository.addTask(addTask.taskName).await())
      }.asResource()
    }.flowOn(dispatchers.io)
    .map { _ -> NoOp }
    .shareIn(viewModelScope, SharingStarted.WhileSubscribed())

  /**
   * StateFlow should basically be a `StateFlow<HomeState>` produced by processing all reducers
   */
  val state = merge(
    loadTasks,
    addTask
  ).scan(HomeState()) { state, reducer -> reducer(state) }
    .flowOn(reducerDispatcher)
    .onEach { logD { "State: $it" } }
    .onCompletion { logD { "State flow completed" } }
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.Lazily,
      initialValue = HomeState()
    )

  private inline fun <reified Action : HomeAction> onAction() = actionsFlow
    // TODO Figure out thread here, in RxJava we can observe using observeOn, but flow only has
    // flowOn
    .filterIsInstance<Action>()
    .onEach { printThread("Action emitted ${it.javaClass.simpleName}") }

  override fun onCleared() {
    reducerDispatcher.close()
  }

  init {
    perform(HomeAction.LoadTasks)
  }

  fun perform(action: HomeAction) {
    viewModelScope.launch {
      actions.emit(action)
    }
  }
}
