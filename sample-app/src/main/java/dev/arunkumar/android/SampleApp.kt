package dev.arunkumar.android

import dagger.android.support.DaggerApplication
import dev.arunkumar.android.di.AppComponent
import dev.arunkumar.android.di.DaggerAppComponent
import dev.arunkumar.android.logging.initDebugLogs

class SampleApp : DaggerApplication() {

    private val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .application(this)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        initDebugLogs()
    }

    override fun applicationInjector() = appComponent
}