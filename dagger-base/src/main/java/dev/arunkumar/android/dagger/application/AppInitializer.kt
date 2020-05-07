package dev.arunkumar.android.dagger.application

import android.app.Application

interface AppInitializer : Comparable<AppInitializer> {

  val priority: Int get() = 0

  fun initialize(application: Application)

  override fun compareTo(other: AppInitializer) = priority.compareTo(other.priority)
}