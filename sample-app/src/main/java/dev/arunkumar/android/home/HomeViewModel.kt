package dev.arunkumar.android.home

import com.babylon.orbit.LifecycleAction
import com.babylon.orbit.OrbitViewModel
import dev.arunkumar.android.data.Item
import dev.arunkumar.android.data.ItemsRepository
import dev.arunkumar.android.result.asResult
import io.realm.kotlin.where
import javax.inject.Inject

class HomeViewModel
@Inject
constructor(
  private val itemsRepository: ItemsRepository
) : OrbitViewModel<HomeState, HomeSideEffect>(HomeState(), {

  perform("Load items")
    .on<LifecycleAction.Created>()
    .transform {
      eventObservable.flatMap {
        itemsRepository
          .pagedItems { it.where<Item>().sort("name") }
          .toObservable()
          .asResult()
      }
    }.reduce { currentState.copy(items = event) }

  perform("delete item")
    .on<HomeAction.DeleteItem>()
    .sideEffect { post(HomeSideEffect.PerformDelete(event.item)) }
})