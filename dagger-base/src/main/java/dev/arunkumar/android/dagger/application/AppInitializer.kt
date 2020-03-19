package dev.arunkumar.android.dagger.application

import android.app.Application

interface AppInitializer {

  fun initialize(application: Application)
}