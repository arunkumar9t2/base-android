package dev.arunkumar.android.dagger.viewmodel

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

/**
 * A module that exposes a [Singleton] binding for [MultibindsViewModelFactory] when [ViewModelProvider.Factory] is
 * requested.
 */
@Module
abstract class DefaultViewModelsBuilder {

    @Binds
    @Singleton
    abstract fun viewModelFactory(factory: MultibindsViewModelFactory): ViewModelProvider.Factory
}