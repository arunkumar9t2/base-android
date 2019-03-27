package dev.arunkumar.android.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dev.arunkumar.android.SampleApp
import dev.arunkumar.android.home.MainActivity
import dev.arunkumar.android.util.PreferencesModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,

        MainActivity.Builder::class,

        /** Utils **/
        PreferencesModule::class
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