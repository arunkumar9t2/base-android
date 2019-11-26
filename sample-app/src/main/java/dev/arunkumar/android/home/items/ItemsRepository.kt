package dev.arunkumar.android.home.items

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import dev.arunkumar.android.realm.RealmDataSourceFactory
import dev.arunkumar.android.realm.epoxy.epoxyBgScheduler
import dev.arunkumar.android.rx.completeable
import dev.arunkumar.android.rxschedulers.SchedulerProvider
import dev.arunkumar.android.util.realm.realmTransaction
import io.reactivex.BackpressureStrategy.LATEST
import io.reactivex.Completable
import io.reactivex.Flowable
import io.realm.kotlin.where
import java.util.*
import javax.inject.Inject

interface ItemsRepository {
    fun addItemsIfEmpty(): Completable

    fun items(
        initialLoadSize: Int = 30 * 3,
        pageSize: Int = 30,
        prefetchDistance: Int = 30 * 2
    ): Flowable<PagedList<Item>>
}

class DefaultItemsRepository
@Inject
constructor(
    private val schedulerProvider: SchedulerProvider
) : ItemsRepository {

    override fun addItemsIfEmpty() = completeable {
        realmTransaction { realm ->
            if (realm.where<Item>().findAll().isEmpty()) {
                val newItems = mutableListOf<Item>().apply {
                    repeat(1000) {
                        add(Item(UUID.randomUUID().toString()))
                    }
                }
                realm.copyToRealmOrUpdate(newItems)
            }
        }
    }

    override fun items(
        initialLoadSize: Int,
        pageSize: Int,
        prefetchDistance: Int
    ): Flowable<PagedList<Item>> {
        val config = PagedList.Config.Builder().run {
            setEnablePlaceholders(false)
            setInitialLoadSizeHint(initialLoadSize)
            setPageSize(pageSize)
            setPrefetchDistance(prefetchDistance)
            build()
        }
        return RxPagedListBuilder(RealmDataSourceFactory { it.where<Item>() }, config).run {
            setFetchScheduler(schedulerProvider.io)
            setNotifyScheduler(epoxyBgScheduler())
        }.buildFlowable(LATEST)
    }
}