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

package dev.arunkumar.realm

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dev.arunkumar.realm.paging.RealmTiledDataSource
import dev.arunkumar.realm.threading.RealmDispatcher
import io.realm.RealmModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion

interface RealmSource<T : RealmModel>

interface PagedRealmSource<T : RealmModel> : RealmSource<T> {

  fun <R> pagedItems(
    initialLoadSize: Int = 10 * 3,
    pageSize: Int = 10,
    prefetchDistance: Int = 10 * 3,
    placeholders: Boolean = false,
    realmQueryBuilder: RealmQueryBuilder<T>
  ): Flow<PagingData<T>> = flow<RealmDispatcher> {
    emit(RealmDispatcher("Paging${this::class.java.simpleName}"))
  }.flatMapConcat { dispatcher ->
    val factory = RealmTiledDataSource.Factory(realmQueryBuilder)
    val pagingSourceFactory = factory.asPagingSourceFactory(dispatcher)
    Pager(
      config = PagingConfig(
        pageSize = pageSize,
        prefetchDistance = prefetchDistance,
        enablePlaceholders = placeholders,
        initialLoadSize = initialLoadSize,
      ),
      initialKey = 0,
      pagingSourceFactory = pagingSourceFactory
    ).flow.onCompletion { dispatcher.stop() }
  }
}
