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

package dev.arunkumar.android.tasks.actions

import android.app.Application
import androidx.work.ListenableWorker
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import dev.arunkumar.android.dagger.workmanager.WorkerKey
import dev.arunkumar.android.logging.logD
import dev.arunkumar.android.tasks.data.TaskRepository
import io.reactivex.Single
import javax.inject.Inject

class DeleteTaskWorker
@Inject
constructor(
  private val taskRepository: TaskRepository,
  application: Application,
  private val workerParams: WorkerParameters
) : RxWorker(application, workerParams) {

  override fun createWork(): Single<Result> {
    val id = workerParams.inputData.keyValueMap["id"] as Int
    return taskRepository
      .deleteTasks(id)
      .toSingle { Result.success() }
      .doOnSuccess {
        logD { "Delete item id $id success" }
      }
  }

  @Module
  interface Builder {
    @Binds
    @IntoMap
    @WorkerKey(DeleteTaskWorker::class)
    fun DeleteTaskWorker.bindWorker(): ListenableWorker
  }
}
