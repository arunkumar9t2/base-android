package dev.arunkumar.android

import dagger.android.support.DaggerApplication
import dev.arunkumar.android.di.AppComponent
import dev.arunkumar.android.di.DaggerAppComponent
import timber.log.Timber

class SampleApp : DaggerApplication() {

    private val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .application(this)
            .build()
    }

    override fun onCreate() {
        super.onCreate()

        initTimber()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun applicationInjector() = appComponent
}