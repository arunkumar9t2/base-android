package dev.arunkumar.android.preferences

import android.app.Application
import com.afollestad.rxkprefs.RxkPrefs
import com.afollestad.rxkprefs.rxkPrefs
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object PreferenceModule {
  @Provides
  @Singleton
  fun rxPrefs(application: Application): RxkPrefs = rxkPrefs(application)
}