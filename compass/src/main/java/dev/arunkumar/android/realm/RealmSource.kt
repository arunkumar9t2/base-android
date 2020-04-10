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
 * Common abstractions for a [RealmObject]
 */
interface RealmSource<T : RealmObject> {

  fun pagedItems(
    initialLoadSize: Int = 30 * 3,
    pageSize: Int = 30,
    prefetchDistance: Int = 30 * 2,
    realmQueryBuilder: (Realm) -> RealmQuery<T>
  ): Flowable<PagedList<T>>

}

abstract class SimpleRealmSource<T : RealmObject>(
  private val schedulerProvider: SchedulerProvider
) : RealmSource<T> {

  override fun pagedItems(
    initialLoadSize: Int,
    pageSize: Int,
    prefetchDistance: Int,
    realmQueryBuilder: (Realm) -> RealmQuery<T>
  ) = deferFlowable {
    val config = PagedList.Config.Builder().run {
      setEnablePlaceholders(true)
      setInitialLoadSizeHint(initialLoadSize)
      setPageSize(pageSize)
      setPrefetchDistance(prefetchDistance)
      build()
    }
    val realmExecutor = RealmExecutor()
    val dataSourceFactory = RealmPagedDataSource.Factory(realmQueryBuilder)

    RxPagedListBuilder(dataSourceFactory, config)
      .run {
        setFetchScheduler(realmExecutor.toScheduler())
        setNotifyScheduler(epoxyBgScheduler())
      }.buildFlowable(BackpressureStrategy.LATEST)
      .doAfterTerminate { realmExecutor.stop() }
  }.subscribeOn(schedulerProvider.io)
}