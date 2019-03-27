package dev.arunkumar.android.home

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.SharedPreferences
import android.os.Bundle
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.DaggerAppCompatActivity
import dagger.multibindings.IntoMap
import dev.arunkumar.android.R
import dev.arunkumar.android.dagger.activity.PerActivity
import dev.arunkumar.android.dagger.viewmodel.UsesViewModel
import dev.arunkumar.android.dagger.viewmodel.ViewModelKey
import dev.arunkumar.android.dagger.viewmodel.viewModel
import javax.inject.Inject

class HomeActivity : DaggerAppCompatActivity(), UsesViewModel {
    @Inject
    lateinit var preferences: SharedPreferences
    @Inject
    lateinit var testActivityScope: DummyActivityScopeDep
    @Inject
    override lateinit var viewModelFactory: ViewModelProvider.Factory

    private val homeViewModel by viewModel<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    @Module
    abstract class Builder {
        @PerActivity
        @ContributesAndroidInjector
        abstract fun mainActivity(): HomeActivity

        @Binds
        @IntoMap
        @ViewModelKey(HomeViewModel::class)
        abstract fun homeViewModel(homeViewModel: HomeViewModel): ViewModel
    }


    @PerActivity
    class DummyActivityScopeDep @Inject constructor()
}
