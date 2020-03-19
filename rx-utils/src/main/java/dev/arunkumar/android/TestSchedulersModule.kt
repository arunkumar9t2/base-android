package dev.arunkumar.android

import dagger.Binds
import dagger.Module
import dev.arunkumar.android.rxschedulers.SchedulerProvider
import dev.arunkumar.android.rxschedulers.TestSchedulerProvider

@Module
abstract class TestSchedulersModule {
  @Binds
  abstract fun schedulerProvider(testSchedulerProvider: TestSchedulerProvider): SchedulerProvider
}