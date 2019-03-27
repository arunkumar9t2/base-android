package dev.arunkumar.android

import dagger.android.support.DaggerApplication
import dev.arunkumar.android.di.AppComponent
import dev.arunkumar.android.di.DaggerAppComponent

class SampleApp : DaggerApplication() {

    private val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .application(this)
            .build()
    }

    override fun applicationInjector() = appComponent
}