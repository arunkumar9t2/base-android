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

package dev.arunkumar.android.dagger.workmanager

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import javax.inject.Provider

abstract class DaggerWorkerFactory<T : WorkerSubComponent.Factory>(
  private val workerSubComponentFactory: T
) : WorkerFactory() {

  override fun createWorker(
    appContext: Context,
    workerClassName: String,
    workerParameters: WorkerParameters
  ): ListenableWorker? {
    val workerSubComponent = workerSubComponentFactory.create(workerParameters)
    return createWorker(workerClassName, workerSubComponent.workers())
  }

  private fun createWorker(
    workerClassName: String,
    workers: Map<Class<out ListenableWorker>, Provider<ListenableWorker>>
  ): ListenableWorker? = try {
    val candidate = workers
      .entries
      .find { Class.forName(workerClassName).isAssignableFrom(it.key) }
    val provider = candidate?.value
      ?: throw IllegalStateException("Missing worker binding for $workerClassName")

    provider.get()
  } catch (e: Exception) {
    throw RuntimeException(e)
  }
}