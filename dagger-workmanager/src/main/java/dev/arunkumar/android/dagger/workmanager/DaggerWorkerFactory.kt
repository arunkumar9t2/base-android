package dev.arunkumar.android.dagger.workmanager

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import javax.inject.Provider

abstract class DaggerWorkerFactory<T : WorkerSubComponent.Factory>(
  private val workerSubComponentFactory: T
) : WorkerFactory() {

  override fun createWorker(
    appContext: Context,
    workerClassName: String,
    workerParameters: WorkerParameters
  ): ListenableWorker? {
    val workerSubComponent = workerSubComponentFactory.create(workerParameters)
    return createWorker(workerClassName, workerSubComponent.workers())
  }

  private fun createWorker(
    workerClassName: String,
    workers: Map<Class<out ListenableWorker>, Provider<ListenableWorker>>
  ): ListenableWorker? = try {
    val candidate = workers
      .entries
      .find { Class.forName(workerClassName).isAssignableFrom(it.key) }
    val provider = candidate?.value
      ?: throw IllegalStateException("Missing worker binding for $workerClassName")

    provider.get()
  } catch (e: Exception) {
    throw RuntimeException(e)
  }
}