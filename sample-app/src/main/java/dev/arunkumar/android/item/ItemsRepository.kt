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

package dev.arunkumar.android.item

import dagger.Binds
import dagger.Module
import dev.arunkumar.android.realm.PagedRealmSource
import dev.arunkumar.android.realm.RealmSource
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

  fun clear(): Completable
}

@Module
interface ItemsModule {
  @Binds
  fun DefaultItemsRepository.itemsRepository(): ItemsRepository
}

class DefaultItemsRepository
@Inject
constructor(
  override val schedulerProvider: SchedulerProvider
) : ItemsRepository, PagedRealmSource<Item> {

  private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

  override fun addItemsIfEmpty() = completable {
    realmTransaction { realm ->
      fun name() = (1..10)
        .map { nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("")

      if (realm.where<Item>().findAll().isEmpty()) {
        val newItems = mutableListOf<Item>().apply {
          for (id in 1..MAX_ITEMS) {
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

  override fun clear() = completable {
    realmTransaction { realm ->
      realm.where<Item>().findAll().deleteAllFromRealm()
    }
  }

  companion object {
    private const val MAX_ITEMS = 30000
  }
}
