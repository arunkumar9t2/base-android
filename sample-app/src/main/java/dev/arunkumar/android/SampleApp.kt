package dev.arunkumar.android

import android.content.Context
import androidx.multidex.MultiDex
import com.airbnb.epoxy.EpoxyController
import dagger.android.support.DaggerApplication
import dev.arunkumar.android.dagger.application.AppInitializer
import dev.arunkumar.android.di.AppComponent
import dev.arunkumar.android.di.DaggerAppComponent
import dev.arunkumar.android.logging.*
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.log.LogLevel.*
import io.realm.log.RealmLog
import javax.inject.Inject

class SampleApp : DaggerApplication() {

  private val appComponent: AppComponent by lazy {
    DaggerAppComponent.builder()
      .application(this)
      .build()
  }

  override fun applicationInjector() = appComponent

  @Inject
  @JvmSuppressWildcards
  lateinit var appInitializers: Set<AppInitializer>

  override fun onCreate() {
    super.onCreate()
    initDebugLogs()
    initEpoxy()
    initRealm()
    appInitializers.forEach { initializer -> initializer.initialize(this) }
  }

  override fun attachBaseContext(base: Context?) {
    super.attachBaseContext(base)
    MultiDex.install(this)
  }

  private fun initEpoxy() {
    EpoxyController.setGlobalDebugLoggingEnabled(BuildConfig.DEBUG)
  }

  private fun initRealm() {
    Realm.init(this)
    RealmLog.add { level, tag, throwable, message ->
      val formattedMessage = "$tag : $message, ${throwable?.message}"
      when (level) {
        DEBUG -> logd(formattedMessage, throwable = throwable)
        INFO -> logi(formattedMessage, throwable = throwable)
        WARN -> logw(formattedMessage, throwable = throwable)
        ERROR -> loge(formattedMessage, throwable = throwable)
        FATAL -> loge(formattedMessage, throwable = throwable)
      }
    }
    RealmConfiguration.Builder()
      .deleteRealmIfMigrationNeeded()
      .build()
      .let(Realm::setDefaultConfiguration)
  }
}