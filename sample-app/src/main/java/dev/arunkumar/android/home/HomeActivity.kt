package dev.arunkumar.android.home

import android.app.Activity
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
import dev.arunkumar.android.home.items.ItemsPagingController
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class HomeActivity : DaggerAppCompatActivity(), UsesViewModel {
  @Inject
  lateinit var testActivityScope: DummyActivityScopeDep

  @Inject
  override lateinit var viewModelFactory: ViewModelProvider.Factory

  @Inject
  lateinit var itemsController: ItemsPagingController

  private val homeViewModel by viewModel<HomeViewModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setupItems()
  }

  private fun setupItems() {
    itemsRv.setController(itemsController)
    homeViewModel.itemsPagedList.subscribeBy(onNext = itemsController::submitList)
    itemsController.clicks.subscribeBy(onNext = homeViewModel::delete)
  }

  @Module
  interface HomeModule {
    @PerActivity
    @Binds
    fun bindActivity(homeActivity: HomeActivity): Activity
  }

  @Module
  interface Builder {
    @PerActivity
    @ContributesAndroidInjector(modules = [HomeModule::class])
    fun homeActivity(): HomeActivity

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    fun homeViewModel(homeViewModel: HomeViewModel): ViewModel
  }


  @PerActivity
  class DummyActivityScopeDep @Inject constructor()
}
