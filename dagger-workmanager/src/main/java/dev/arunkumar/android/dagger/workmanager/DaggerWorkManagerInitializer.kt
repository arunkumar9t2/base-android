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

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import dev.arunkumar.android.dagger.application.AppInitializer
import dev.arunkumar.android.logging.logd
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DaggerWorkManagerInitializer
@Inject
constructor(private val workerFactory: WorkerFactory) : AppInitializer {

  override val priority: Int = 0

  private fun workManagerConfig(block: Configuration.Builder.() -> Unit): Configuration {
    return Configuration.Builder().apply(block).build()
  }

  override fun initialize(application: Application) {
    val config = workManagerConfig {
      setWorkerFactory(workerFactory)
    }
    WorkManager.initialize(application, config)
    logd("Workmanager initialized successfully")
  }
}