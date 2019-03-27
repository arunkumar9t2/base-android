package dev.arunkumar.android.dagger.activity

import android.app.Activity
import javax.inject.Scope

/**
 * Scope to denote that an object's lasts for the duration of [Activity] instance.
 */
@Scope
@Retention
annotation class PerActivity