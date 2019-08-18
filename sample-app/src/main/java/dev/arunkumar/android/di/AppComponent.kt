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
import dev.arunkumar.android.util.PreferencesModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,

        HomeActivity.Builder::class,

        /** Utils **/
        DefaultViewModelsBuilder::class,
        PreferencesModule::class,
        AppSchedulersModule::class
    ]
)
interface AppComponent : AndroidInjector<SampleApp> {

    @Component.Builder
    interface Builder {
        fun build(): AppComponent
        @BindsInstance
        fun application(application: Application): Builder
    }
}