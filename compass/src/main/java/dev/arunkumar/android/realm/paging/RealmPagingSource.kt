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

package dev.arunkumar.android.realm.paging

import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import dev.arunkumar.android.realm.DefaultRealm
import dev.arunkumar.android.realm.RealmQueryBuilder
import dev.arunkumar.android.realm.threading.RealmExecutor
import dev.arunkumar.android.rx.createSingle
import dev.arunkumar.android.rxschedulers.toScheduler
import io.reactivex.Single
import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmResults

class RealmPagingSource<T : RealmModel>(
  private val realmQueryBuilder: RealmQueryBuilder<T>
) : RxPagingSource<Int, T>() {
  private val realmExecutor = RealmExecutor(tag = "RealmPagingExecutor")
  private val realmScheduler = realmExecutor.toScheduler()

  private val realm: Realm by lazy { DefaultRealm() }
  private val realmQuery by lazy { realm.realmQueryBuilder() }
  private var realmChangeListener = { _: RealmResults<T> ->
    invalidate()
  }

  private val realmResults by lazy {
    realmQuery.findAll().apply {
      addChangeListener(realmChangeListener)
    }
  }

  init {
    registerInvalidatedCallback {
      if (realmResults.isValid) {
        realmResults.removeAllChangeListeners()
      }
      realm.close()
      realmExecutor.stop()
    }
  }

  override fun getRefreshKey(state: PagingState<Int, T>): Int? {
    return state.anchorPosition?.let { anchorPosition ->
      val anchorPage = state.closestPageToPosition(anchorPosition)
      anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
    }
  }

  private val size
    get() = when {
      realm.isClosed || !realmResults.isValid -> 0
      else -> realmResults.size
    }

  override fun loadSingle(
    params: LoadParams<Int>
  ): Single<LoadResult<Int, T>> {
    return loadPage(params.key, params.loadSize)
      .subscribeOn(realmScheduler)
  }

  private fun loadPage(
    pageNo: Int?,
    loadSize: Int,
  ): Single<LoadResult<Int, T>> = createSingle {
    if (pageNo == null) {
      onSuccess(
        LoadResult.Page(
          data = emptyList(),
          prevKey = null,
          nextKey = null
        )
      )
    } else {
      val startPosition = pageNo * loadSize
      val endPosition = minOf(startPosition + loadSize, size)
      val loadResult = LoadResult.Page(
        data = buildList<T> {
          for (position in startPosition until endPosition) {
            realmResults[position]?.let { item ->
              add(realm.copyFromRealm(item))
            }
          }
        },
        prevKey = null,
        nextKey = pageNo + 1
      )
      onSuccess(loadResult)
    }
  }
}
