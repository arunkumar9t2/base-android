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
import androidx.paging.cachedIn
import dev.arunkumar.android.logging.logD
import dev.arunkumar.android.tasks.actions.ResetAllTasks
import dev.arunkumar.android.tasks.data.Task
import dev.arunkumar.android.tasks.data.TaskRepository
import dev.arunkumar.android.util.DispatcherProvider
import dev.arunkumar.android.util.printThread
import dev.arunkumar.android.util.resourceFlow
import dev.arunkumar.common.result.Resource
import io.realm.kotlin.where
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.rx2.await
import java.util.*
import javax.inject.Inject

data class HomeState(
  val tasks: Flow<PagingData<Task>> = flowOf(PagingData.empty()),
  val reset: Resource<Unit> = Resource.Success(Unit)
)

sealed class HomeSideEffect

sealed class HomeAction {
  object LoadTasks : HomeAction()
  data class AddTask(val taskName: String) : HomeAction()
  data class CompleteTask(val taskId: UUID, val completed: Boolean) : HomeAction()
  data class DeleteTask(val taskId: UUID) : HomeAction()
  object ResetTasks : HomeAction()
}

private typealias HomeReducer = HomeState.() -> HomeState

class HomeViewModel
@Inject
constructor(
  dispatchers: DispatcherProvider,
  taskRepository: TaskRepository,
  private val resetAllTask: ResetAllTasks
) : ViewModel() {

  private val reducerDispatcher = newSingleThreadContext("Reducer")

  @Suppress("NOTHING_TO_INLINE")
  private inline fun Reducer(noinline reducer: HomeState.() -> HomeState) = reducer

  private fun <T> Flow<T>.mapToReducer(
    transform: HomeState.(value: T) -> HomeState = { this }
  ) = map { value -> Reducer { transform(value) } }

  private fun <T> Flow<T>.share() = shareIn(viewModelScope, SharingStarted.WhileSubscribed())

  /**
   * Action stream for processing set of UI actions received from View
   */
  private val actions = MutableSharedFlow<HomeAction>()
  private val actionsFlow = actions.asSharedFlow()

  // TODO Implement one off without caching but lifecycle aware like LiveData
  private val _effects = MutableSharedFlow<HomeSideEffect>()
  val effects = _effects.asSharedFlow()

  // Actions to reducers
  private val loadTasks: Flow<HomeReducer> = onAction<HomeAction.LoadTasks>()
    .onStart { emit(HomeAction.LoadTasks) }
    .mapLatest {
      taskRepository.addItemsIfEmpty().await()
      taskRepository
        .pagedItems<Task> {
          where<Task>().sort("name")
        }.cachedIn(viewModelScope)
    }.flowOn(dispatchers.io)
    .mapToReducer { pagedTasks -> copy(tasks = pagedTasks) }
    .share()

  private val addTask: Flow<HomeReducer> = onAction<HomeAction.AddTask>()
    .flatMapLatest { addTask ->
      resourceFlow {
        emit(taskRepository.addTask(addTask.taskName).await())
      }
    }.flowOn(dispatchers.io)
    .mapToReducer()
    .share()

  private val completeTask: Flow<HomeReducer> = onAction<HomeAction.CompleteTask>()
    .onEach { completeTask ->
      taskRepository
        .completeTask(completeTask.taskId, completeTask.completed)
        .await()
    }.flowOn(dispatchers.io)
    .mapToReducer()
    .share()

  private val deleteTask: Flow<HomeReducer> = onAction<HomeAction.DeleteTask>()
    .onEach { deleteTask ->
      taskRepository.deleteTasks(deleteTask.taskId).await()
    }.flowOn(dispatchers.io)
    .mapToReducer()
    .share()

  private val resetTasks: Flow<HomeReducer> = onAction<HomeAction.ResetTasks>()
    .debounce(500)
    .conflate()
    .flatMapLatest { resourceFlow { emit(resetAllTask.build().await()) } }
    .flowOn(dispatchers.io)
    .mapToReducer { reset -> copy(reset = reset) }
    .share()

  /**
   * StateFlow should basically be a `StateFlow<HomeState>` produced by processing all reducers
   */
  val state = merge(
    loadTasks,
    addTask,
    completeTask,
    deleteTask,
    resetTasks
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
