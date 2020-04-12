package dev.arunkumar.android.home

import com.afollestad.rxkprefs.RxkPrefs
import com.afollestad.rxkprefs.rxjava.observe
import com.babylon.orbit.LifecycleAction
import com.babylon.orbit.OrbitViewModel
import dev.arunkumar.android.item.Item
import dev.arunkumar.android.item.ItemsRepository
import dev.arunkumar.android.item.ResetDbUseCase
import dev.arunkumar.android.preferences.Preference
import dev.arunkumar.android.result.asResource
import io.realm.kotlin.where
import javax.inject.Inject

class HomeViewModel
@Inject
constructor(
  private val rxkPrefs: RxkPrefs,
  private val itemsRepository: ItemsRepository,
  private val resetDbUseCase: ResetDbUseCase
) : OrbitViewModel<HomeState, HomeSideEffect>(HomeState(), {

  val sortPreference: Preference<Boolean> by lazy {
    val sortPreferenceId = "sortPreference"
    Preference<Boolean>(
      sortPreferenceId,
      "Sort items"
    ).apply {
      value = rxkPrefs.boolean(sortPreferenceId)
    }
  }

  val headerPreference: Preference<Boolean> by lazy {
    val headerPreferenceId = "headerPreference"
    Preference<Boolean>(
      headerPreferenceId,
      "Show headers"
    ).apply {
      value = rxkPrefs.boolean(headerPreferenceId)
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
            itemsRepository.pagedItems<Item> { realm ->
              realm.where<Item>().let {
                if (sort) {
                  it.sort("name")
                } else it
              }
            }.toObservable().asResource()
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
    .transform { eventObservable.flatMap { resetDbUseCase.build().toObservable<Any>() } }
})