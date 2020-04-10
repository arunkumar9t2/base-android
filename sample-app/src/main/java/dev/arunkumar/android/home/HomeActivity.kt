package dev.arunkumar.android.home

import android.app.Activity
import android.os.Bundle
import androidx.lifecycle.Lifecycle.Event.ON_DESTROY
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.uber.autodispose.android.lifecycle.autoDispose
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
import dev.arunkumar.android.data.DeleteItemWorker
import dev.arunkumar.android.home.items.ItemsPagingController
import dev.arunkumar.android.itemanimator.SpringSlideInItemAnimator
import dev.arunkumar.common.result.Result
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class HomeActivity : DaggerAppCompatActivity(), UsesViewModel {
  @Inject
  lateinit var testActivityScope: DummyActivityScopeDep

  @Inject
  override lateinit var viewModelFactory: ViewModelProvider.Factory

  @Inject
  lateinit var itemsController: ItemsPagingController

  @Inject
  lateinit var workManager: WorkManager

  private val homeViewModel by viewModel<HomeViewModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setupItems()
    homeViewModel.connect(
      this,
      stateConsumer,
      sideEffectConsumer
    )
  }

  private fun setupItems() {
    itemsRv.setController(itemsController)
    itemsRv.run {
      setController(itemsController)
      itemAnimator = SpringSlideInItemAnimator()
    }
    itemsController.clicks
      .map { HomeAction.DeleteItem(it) }
      .autoDispose(this, ON_DESTROY)
      .subscribe(homeViewModel::sendAction)
  }


  private val stateConsumer: (HomeState) -> Unit = { homeState ->
    when (homeState.items) {
      // handle other cases
      is Result.Success -> itemsController.submitList(homeState.items.data)
    }
  }

  private val sideEffectConsumer: (HomeSideEffect) -> Unit = { homeSideEffect ->
    when (homeSideEffect) {
      is HomeSideEffect.PerformDelete -> deleteItem(homeSideEffect)
    }
  }

  private fun deleteItem(homeSideEffect: HomeSideEffect.PerformDelete) {
    val workRequest = OneTimeWorkRequestBuilder<DeleteItemWorker>().run {
      setInputData(workDataOf("id" to homeSideEffect.item.id))
      build()
    }
    workManager.enqueue(workRequest)
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
