package dev.arunkumar.android.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dev.arunkumar.android.AppSchedulersModule
import dev.arunkumar.android.SampleApp
import dev.arunkumar.android.dagger.viewmodel.DefaultViewModelsBuilder
import dev.arunkumar.android.data.ItemsModule
import dev.arunkumar.android.home.HomeActivity
import dev.arunkumar.android.util.PreferencesModule
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
    PreferencesModule::class,
    AppSchedulersModule::class,
    SampleDaggerWorkerFactory.Module::class
  ]
)
interface AppComponent : AndroidInjector<SampleApp> {

  fun workerSubComponentFactory(): SampleWorkerSubComponent.Factory

  @Component.Builder
  interface Builder {
    fun build(): AppComponent

    @BindsInstance
    fun application(application: Application): Builder
  }
}