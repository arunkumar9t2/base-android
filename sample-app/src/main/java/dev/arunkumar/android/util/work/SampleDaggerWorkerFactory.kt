package dev.arunkumar.android.util.work

import androidx.work.WorkerFactory
import dagger.Binds
import dev.arunkumar.android.dagger.workmanager.DaggerWorkerFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SampleDaggerWorkerFactory
@Inject
constructor(
  factory: SampleWorkerSubComponent.Factory
) : DaggerWorkerFactory<SampleWorkerSubComponent.Factory>(factory) {

  @dagger.Module
  interface Module {
    @Binds
    fun workerFactor(sampleDaggerWorkerFactory: SampleDaggerWorkerFactory): WorkerFactory
  }
}