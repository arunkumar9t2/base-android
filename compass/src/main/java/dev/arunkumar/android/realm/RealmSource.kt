package dev.arunkumar.android.realm

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import dev.arunkumar.android.realm.epoxy.epoxyBgScheduler
import dev.arunkumar.android.realm.threading.RealmExecutor
import dev.arunkumar.android.rx.deferFlowable
import dev.arunkumar.android.rxschedulers.SchedulerProvider
import dev.arunkumar.android.rxschedulers.toScheduler
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.realm.Realm
import io.realm.RealmObject
import io.realm.RealmQuery

/**
 * Common abstractions for a [RealmObject] providing paging lib support to Realm objects
 */
interface RealmSource<T : RealmObject> {

  @Suppress("UNCHECKED_CAST")
  fun <R> pagedItems(
    initialLoadSize: Int = 30 * 3,
    pageSize: Int = 30,
    prefetchDistance: Int = 30 * 2,
    mapper: (T) -> R = { it as R },
    realmQueryBuilder: (Realm) -> RealmQuery<T>
  ): Flowable<PagedList<R>>

}

interface SimpleRealmSource<T : RealmObject> : RealmSource<T> {

  val schedulerProvider: SchedulerProvider

  override fun <R> pagedItems(
    initialLoadSize: Int,
    pageSize: Int,
    prefetchDistance: Int,
    mapper: (T) -> R,
    realmQueryBuilder: (Realm) -> RealmQuery<T>
  ): Flowable<PagedList<R>> = deferFlowable {
    val config = PagedList.Config.Builder().run {
      setEnablePlaceholders(true)
      setInitialLoadSizeHint(initialLoadSize)
      setPageSize(pageSize)
      setPrefetchDistance(prefetchDistance)
      build()
    }
    val realmExecutor = RealmExecutor()
    val dataSourceFactory = RealmPagedDataSource.Factory(realmQueryBuilder)
      .map { mapper(it) }

    RxPagedListBuilder(dataSourceFactory, config)
      .run {
        setFetchScheduler(realmExecutor.toScheduler())
        setNotifyScheduler(epoxyBgScheduler())
      }.buildFlowable(BackpressureStrategy.LATEST)
      .doAfterTerminate { realmExecutor.stop() }
  }.subscribeOn(schedulerProvider.io)
}