package dev.arunkumar.android.data

import dagger.Binds
import dagger.Module
import dev.arunkumar.android.realm.RealmSource
import dev.arunkumar.android.realm.SimpleRealmSource
import dev.arunkumar.android.realm.realmTransaction
import dev.arunkumar.android.rx.completable
import dev.arunkumar.android.rxschedulers.SchedulerProvider
import io.reactivex.Completable
import io.realm.kotlin.where
import javax.inject.Inject
import kotlin.random.Random.Default.nextInt

interface ItemsRepository : RealmSource<Item> {
  fun addItemsIfEmpty(): Completable

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
  schedulerProvider: SchedulerProvider
) : ItemsRepository, SimpleRealmSource<Item>(schedulerProvider) {

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

  override fun deleteItem(itemId: Int) = completable {
    realmTransaction { realm ->
      realm
        .where<Item>()
        .equalTo("id", itemId)
        .findAll()
        .deleteAllFromRealm()
    }
  }
}