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

package dev.arunkumar.android.di

import android.app.Application
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.multibindings.ElementsIntoSet
import dev.arunkumar.android.AppSchedulersModule
import dev.arunkumar.android.dagger.application.AppInitializer
import dev.arunkumar.android.dagger.viewmodel.DefaultViewModelsBuilder
import dev.arunkumar.android.dagger.workmanager.DaggerWorkManagerInitializer
import dev.arunkumar.android.preferences.PreferenceModule
import dev.arunkumar.android.util.DispatcherModule
import dev.arunkumar.android.util.work.SampleDaggerWorkerFactory
import javax.inject.Singleton

@Module(
  includes = [
    DispatcherModule::class,
    PreferenceModule::class,
    AppSchedulersModule::class,
    DefaultViewModelsBuilder::class,
    SampleDaggerWorkerFactory.Module::class
  ]
)
object AppModule {
  @Provides
  @ElementsIntoSet
  fun initializer(
    daggerWorkManagerInitializer: DaggerWorkManagerInitializer
  ): Set<AppInitializer> = linkedSetOf(daggerWorkManagerInitializer)

  @Singleton
  @Provides
  fun workManager(application: Application): WorkManager = WorkManager.getInstance(application)
}
