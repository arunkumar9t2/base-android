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
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dev.arunkumar.android.AppSchedulersModule
import dev.arunkumar.android.SampleApp
import dev.arunkumar.android.dagger.viewmodel.DefaultViewModelsBuilder
import dev.arunkumar.android.home.HomeActivity
import dev.arunkumar.android.item.ItemsModule
import dev.arunkumar.android.preferences.PreferenceModule
import dev.arunkumar.android.util.work.SampleDaggerWorkerFactory
import dev.arunkumar.android.util.work.SampleWorkerSubComponent
import javax.inject.Singleton

@Singleton
@Component(
  modules = [
    AndroidSupportInjectionModule::class,

    HomeActivity.Builder::class,

    /** Items **/
    ItemsModule::class,

    /** Utils **/
    AppModule::class,
    DefaultViewModelsBuilder::class,
    PreferenceModule::class,
    AppSchedulersModule::class,
    SampleDaggerWorkerFactory.Module::class
  ]
)
interface AppComponent : AndroidInjector<SampleApp> {

  fun workerSubComponentFactory(): SampleWorkerSubComponent.Factory

  @Component.Factory
  interface Builder {
    fun create(@BindsInstance application: Application): AppComponent
  }
}
