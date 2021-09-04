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

package dev.arunkumar.android.tasks.data

import dagger.Binds
import dagger.Module
import dev.arunkumar.android.realm.PagedRealmSource
import dev.arunkumar.android.realm.RealmTransaction
import dev.arunkumar.android.rx.completable
import dev.arunkumar.android.rx.createSingle
import io.reactivex.Completable
import io.reactivex.Single
import io.realm.kotlin.where
import java.util.*
import javax.inject.Inject
import kotlin.random.Random.Default.nextInt

interface TaskRepository : PagedRealmSource<Task> {

  fun addItemsIfEmpty(): Completable

  fun addTask(taskName: String): Single<Task>

  fun deleteTasks(taskId: UUID): Completable

  fun completeTask(taskId: UUID, completed: Boolean): Completable

  fun clear(): Completable
}

@Module
interface TasksModule {
  @Binds
  fun DefaultTaskRepository.taskRepository(): TaskRepository
}

class DefaultTaskRepository
@Inject
constructor() : TaskRepository {

  private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

  override fun addItemsIfEmpty() = completable {
    RealmTransaction { realm ->
      fun name() = (1..10)
        .map { nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("")

      if (realm.where<Task>().findAll().isEmpty()) {
        val newItems = mutableListOf<Task>().apply {
          for (id in 1..MAX_ITEMS) {
            add(Task(UUID.randomUUID(), name()))
          }
        }
        realm.copyToRealmOrUpdate(newItems)
      }
    }
  }

  override fun addTask(taskName: String): Single<Task> = createSingle {
    try {
      RealmTransaction { realm ->
        onSuccess(realm.copyToRealm(Task(name = taskName)))
      }
    } catch (e: Exception) {
      tryOnError(e)
    }
  }

  override fun deleteTasks(taskId: UUID) = completable {
    RealmTransaction { realm ->
      realm
        .where<Task>()
        .equalTo("id", taskId)
        .findAll()
        .deleteAllFromRealm()
    }
  }

  override fun completeTask(taskId: UUID, completed: Boolean) = completable {
    RealmTransaction { realm ->
      realm.where<Task>()
        .equalTo("id", taskId)
        .findFirst()
        ?.completed = completed
    }
  }

  override fun clear() = completable {
    RealmTransaction { realm ->
      realm.where<Task>().findAll().deleteAllFromRealm()
    }
  }

  companion object {
    private const val MAX_ITEMS = 3000
  }
}
