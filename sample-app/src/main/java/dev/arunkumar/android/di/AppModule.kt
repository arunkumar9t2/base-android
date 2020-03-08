package dev.arunkumar.android.di

import android.app.Application
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
interface AppModule {

    @Module
    companion object {
        @JvmStatic
        @Singleton
        @Provides
        fun workManager(application: Application): WorkManager =
            WorkManager.getInstance(application)
    }
}