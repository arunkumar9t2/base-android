package dev.arunkumar.android

import dagger.Binds
import dagger.Module
import dev.arunkumar.android.rxschedulers.AppSchedulerProvider
import dev.arunkumar.android.rxschedulers.SchedulerProvider

@Module
abstract class AppSchedulersModule {
    @Binds
    abstract fun schedulerProvider(appSchedulerProvider: AppSchedulerProvider): SchedulerProvider
}