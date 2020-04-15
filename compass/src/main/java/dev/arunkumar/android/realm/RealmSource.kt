package dev.arunkumar.android.realm

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import dev.arunkumar.android.realm.paging.pagingConfig
import dev.arunkumar.android.realm.threading.RealmExecutor
import dev.arunkumar.android.rx.deferObservable
import dev.arunkumar.android.rxschedulers.SchedulerProvider
import dev.arunkumar.android.rxschedulers.toScheduler
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.realm.Realm
import io.realm.RealmObject
import io.realm.RealmQuery

/**
 * Common abstractions for a [RealmObject] providing paging lib support to Realm objects
 */
interface RealmSource<T : RealmObject> {

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

interface SimpleRealmSource<T : RealmObject> : RealmSource<T> {

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
      .run {
        setFetchScheduler(realmExecutor.toScheduler())
        setNotifyScheduler(notifyScheduler)
      }.buildObservable()
      .doAfterTerminate { realmExecutor.stop() }
  }.subscribeOn(schedulerProvider.io)
}