/*
 * Copyright 2021 Arunkumar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
    DaggerAppComponent.factory().create(this)
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
        DEBUG -> logD(throwable = throwable) { formattedMessage }
        INFO -> logI(throwable = throwable) { formattedMessage }
        WARN -> logW(throwable = throwable) { formattedMessage }
        ERROR -> logE(throwable = throwable) { formattedMessage }
        FATAL -> logE(throwable = throwable) { formattedMessage }
      }
    }
    val realmConfiguration = RealmConfiguration.Builder()
      .deleteRealmIfMigrationNeeded()
      .allowQueriesOnUiThread(false)
      .allowWritesOnUiThread(false)
      .build()
    Realm.setDefaultConfiguration(realmConfiguration)
  }
}
