package dev.arunkumar.android.di

import android.app.Application
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.multibindings.ElementsIntoSet
import dev.arunkumar.android.dagger.application.AppInitializer
import dev.arunkumar.android.dagger.workmanager.DaggerWorkManagerInitializer
import javax.inject.Singleton

@Module
interface AppModule {
  @Module
  companion object {
    @Provides
    @JvmStatic
    @ElementsIntoSet
    fun initializer(
      daggerWorkManagerInitializer: DaggerWorkManagerInitializer
    ): Set<AppInitializer> = linkedSetOf(daggerWorkManagerInitializer)

    @JvmStatic
    @Singleton
    @Provides
    fun workManager(application: Application): WorkManager =
      WorkManager.getInstance(application)
  }
}