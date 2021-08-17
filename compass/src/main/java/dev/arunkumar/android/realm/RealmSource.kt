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

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import dev.arunkumar.android.realm.paging.RealmPagedDataSource
import dev.arunkumar.android.realm.paging.RealmTiledDataSource
import dev.arunkumar.android.realm.paging.pagingConfig
import dev.arunkumar.android.realm.threading.RealmExecutor
import dev.arunkumar.android.rx.deferObservable
import dev.arunkumar.android.rxschedulers.SchedulerProvider
import dev.arunkumar.android.rxschedulers.toScheduler
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmQuery

/**
 * Common abstractions for a [RealmModel] providing paging support to [Realm] objects
 */
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
  ): Observable<PagedList<R>>

}

interface TiledRealmSource<T : RealmModel> : RealmSource<T> {

  override fun <R> pagedItems(
    initialLoadSize: Int,
    pageSize: Int,
    prefetchDistance: Int,
    placeholders: Boolean,
    notifyScheduler: Scheduler,
    itemMapper: (T) -> R,
    realmQueryBuilder: (Realm) -> RealmQuery<T>
  ): Observable<PagedList<R>> = deferObservable {
    val pagingConfig = pagingConfig {
      setEnablePlaceholders(placeholders)
      setInitialLoadSizeHint(initialLoadSize)
      setPageSize(pageSize)
      setPrefetchDistance(prefetchDistance)
    }
    val realmTiledDataSourceFactory = RealmTiledDataSource.Factory(realmQueryBuilder)
      .map { itemMapper(it) }

    val realmExecutor = RealmExecutor()

    RxPagedListBuilder(realmTiledDataSourceFactory, pagingConfig)
      .setFetchScheduler(realmExecutor.toScheduler())
      .setNotifyScheduler(notifyScheduler)
      .buildObservable()
      .doAfterTerminate { realmExecutor.stop() }
  }.subscribeOn(schedulerProvider.io)
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
  ): Observable<PagedList<R>> = deferObservable {
    val pagingConfig = pagingConfig {
      setEnablePlaceholders(placeholders)
      setInitialLoadSizeHint(initialLoadSize)
      setPageSize(pageSize)
      setPrefetchDistance(prefetchDistance)
    }
    val realmPagedDataSourceFactory = RealmPagedDataSource.Factory(realmQueryBuilder)
      .map { itemMapper(it) }

    val realmExecutor = RealmExecutor()

    RxPagedListBuilder(realmPagedDataSourceFactory, pagingConfig)
      .setFetchScheduler(realmExecutor.toScheduler())
      .setNotifyScheduler(notifyScheduler)
      .buildObservable()
      .doAfterTerminate { realmExecutor.stop() }
  }.subscribeOn(schedulerProvider.io)
}
