package dev.arunkumar.android.realm.paging

import androidx.paging.PositionalDataSource

abstract class TiledDataSource<T> : PositionalDataSource<T>() {

  abstract fun countItems(): Int

  abstract fun loadRange(startPosition: Int, count: Int): List<T>?

  override fun loadInitial(
    params: LoadInitialParams,
    callback: LoadInitialCallback<T>
  ) {
    val totalCount = countItems()
    if (totalCount == 0) {
      callback.onResult(emptyList(), 0, 0)
      return
    }

    val firstLoadPosition = computeInitialLoadPosition(params, totalCount)
    val firstLoadSize = computeInitialLoadSize(params, firstLoadPosition, totalCount)

    val list = loadRange(firstLoadPosition, firstLoadSize)
    if (list != null && list.size == firstLoadSize) {
      callback.onResult(list, firstLoadPosition, totalCount)
    } else {
      invalidate()
    }
  }

  override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<T>) {
    val list = loadRange(params.startPosition, params.loadSize)
    if (list != null) {
      callback.onResult(list)
    } else {
      invalidate()
    }
  }
}