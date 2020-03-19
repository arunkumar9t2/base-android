package dev.arunkumar.android.data

import androidx.paging.PagedList
import androidx.paging.PagedList.Config
import androidx.paging.RxPagedListBuilder
import dagger.Binds
import dagger.Module
import dev.arunkumar.android.realm.RealmDataSourceFactory
import dev.arunkumar.android.realm.epoxy.epoxyBgScheduler
import dev.arunkumar.android.realm.realmTransaction
import dev.arunkumar.android.realm.threading.RealmExecutor
import dev.arunkumar.android.rx.completable
import dev.arunkumar.android.rxschedulers.SchedulerProvider
import dev.arunkumar.android.rxschedulers.toScheduler
import io.reactivex.BackpressureStrategy.LATEST
import io.reactivex.Completable
import io.reactivex.Flowable
import io.realm.Realm
import io.realm.RealmQuery
import io.realm.kotlin.where
import javax.inject.Inject
import kotlin.random.Random.Default.nextInt

interface ItemsRepository {
  fun addItemsIfEmpty(): Completable

  fun items(
    initialLoadSize: Int = 30 * 3,
    pageSize: Int = 30,
    prefetchDistance: Int = 30 * 2
  ): Flowable<PagedList<Item>>

  fun deleteItem(itemId: Int): Completable
}

@Module
interface ItemsModule {
  @Binds
  fun itemsRepository(defaultItemsRepository: DefaultItemsRepository): ItemsRepository
}

class DefaultItemsRepository
@Inject
constructor(
  private val schedulerProvider: SchedulerProvider
) : ItemsRepository {

  private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

  override fun addItemsIfEmpty() = completable {
    realmTransaction { realm ->
      fun name() = (1..10)
        .map { nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("")

      if (realm.where<Item>().findAll().isEmpty()) {
        val newItems = mutableListOf<Item>().apply {
          for (id in 1..1000) {
            add(Item(id, name()))
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
  ) = addItemsIfEmpty()
    .subscribeOn(schedulerProvider.io)
    .andThen(Flowable.defer<PagedList<Item>> {
      val config = Config.Builder().run {
        setEnablePlaceholders(false)
        setInitialLoadSizeHint(initialLoadSize)
        setPageSize(pageSize)
        setPrefetchDistance(prefetchDistance)
        build()
      }
      val realmExecutor = RealmExecutor()
      val realmQueryBuilder: (Realm) -> RealmQuery<Item> = {
        it.where<Item>().sort("name")
      }
      val dataSourceFactory = RealmDataSourceFactory(realmQueryBuilder)

      RxPagedListBuilder(dataSourceFactory, config)
        .run {
          setFetchScheduler(realmExecutor.toScheduler())
          setNotifyScheduler(epoxyBgScheduler())
        }.buildFlowable(LATEST)
        .doAfterTerminate { realmExecutor.stop() }
    })

  override fun deleteItem(itemId: Int) = completable {
    realmTransaction {
      it.where<Item>().equalTo("id", itemId).findAll().deleteAllFromRealm()
    }
  }
}