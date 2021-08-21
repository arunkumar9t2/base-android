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

package dev.arunkumar.android.realm

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dev.arunkumar.android.realm.paging.RealmPagingSource
import dev.arunkumar.android.rxschedulers.SchedulerProvider
import io.reactivex.Scheduler
import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmQuery
import kotlinx.coroutines.flow.Flow

interface RealmSource<T : RealmModel> {

  val schedulerProvider: SchedulerProvider

  @Suppress("UNCHECKED_CAST")
  fun <R> pagedItems(
    initialLoadSize: Int = 30 * 3,
    pageSize: Int = 30,
    prefetchDistance: Int = 30 * 2,
    placeholders: Boolean = false,
    notifyScheduler: Scheduler = schedulerProvider.ui,
    itemMapper: (T) -> R = { it as R },
    realmQueryBuilder: (Realm) -> RealmQuery<T>
  ): Flow<PagingData<T>>

}

interface PagedRealmSource<T : RealmModel> : RealmSource<T> {

  override fun <R> pagedItems(
    initialLoadSize: Int,
    pageSize: Int,
    prefetchDistance: Int,
    placeholders: Boolean,
    notifyScheduler: Scheduler,
    itemMapper: (T) -> R,
    realmQueryBuilder: (Realm) -> RealmQuery<T>
  ): Flow<PagingData<T>> {
    return Pager(
      config = PagingConfig(
        pageSize = pageSize,
        prefetchDistance = prefetchDistance,
        enablePlaceholders = placeholders,
        initialLoadSize = initialLoadSize,
      ),
      initialKey = 0,
      pagingSourceFactory = {
        RealmPagingSource(realmQueryBuilder)
      }
    ).flow
  }
}
