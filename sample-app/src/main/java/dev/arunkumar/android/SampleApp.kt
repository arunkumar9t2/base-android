package dev.arunkumar.android

import com.airbnb.epoxy.EpoxyAsyncUtil.getAsyncBackgroundHandler
import com.airbnb.epoxy.EpoxyController
import dagger.android.support.DaggerApplication
import dev.arunkumar.android.di.AppComponent
import dev.arunkumar.android.di.DaggerAppComponent
import dev.arunkumar.android.logging.*
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.log.LogLevel.*
import io.realm.log.RealmLog

class SampleApp : DaggerApplication() {

    private val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .application(this)
            .build()
    }

    override fun applicationInjector() = appComponent

    override fun onCreate() {
        super.onCreate()
        initDebugLogs()

        initEpoxy()
        initRealm()
    }

    private fun initEpoxy() {
        EpoxyController.setGlobalDebugLoggingEnabled(BuildConfig.DEBUG)
        EpoxyController.defaultDiffingHandler = getAsyncBackgroundHandler()
        EpoxyController.defaultModelBuildingHandler = getAsyncBackgroundHandler()
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