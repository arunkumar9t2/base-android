package dev.arunkumar.android.home

import androidx.paging.PagedList
import dev.arunkumar.android.data.Item
import dev.arunkumar.common.result.Result
import dev.arunkumar.common.result.idle

data class HomeState(
  val items: Result<PagedList<Item>> = idle()
)

sealed class HomeSideEffect {
  data class PerformDelete(val item: Item) : HomeSideEffect()
}

sealed class HomeAction {
  data class DeleteItem(val item: Item) : HomeAction()
}