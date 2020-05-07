package dev.arunkumar.android.dagger.workmanager

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import dev.arunkumar.android.dagger.application.AppInitializer
import dev.arunkumar.android.logging.logd
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DaggerWorkManagerInitializer
@Inject
constructor(private val workerFactory: WorkerFactory) : AppInitializer {

  override val priority: Int = 0

  private fun workManagerConfig(block: Configuration.Builder.() -> Unit): Configuration {
    return Configuration.Builder().apply(block).build()
  }

  override fun initialize(application: Application) {
    val config = workManagerConfig {
      setWorkerFactory(workerFactory)
    }
    WorkManager.initialize(application, config)
    logd("Workmanager initialized successfully")
  }
}