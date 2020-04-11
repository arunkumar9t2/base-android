package dev.arunkumar.android.home

import androidx.paging.PagedList
import dev.arunkumar.android.item.Item
import dev.arunkumar.android.preferences.Preference
import dev.arunkumar.common.result.Resource
import dev.arunkumar.common.result.idle

data class HomeState(
  val preferences: List<Preference<*>> = emptyList(),
  val headers: Boolean = true,
  val items: Resource<PagedList<Item>> = idle()
)

sealed class HomeSideEffect {
  data class PerformDelete(val item: Item) : HomeSideEffect()
}

sealed class HomeAction {
  data class DeleteItem(val item: Item) : HomeAction()
  object ResetItems : HomeAction()
}