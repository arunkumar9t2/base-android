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

package dev.arunkumar.android.home

import android.app.Activity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.DaggerAppCompatActivity
import dagger.multibindings.IntoMap
import dev.arunkumar.android.dagger.activity.PerActivity
import dev.arunkumar.android.dagger.viewmodel.UsesViewModel
import dev.arunkumar.android.dagger.viewmodel.ViewModelKey
import dev.arunkumar.android.dagger.viewmodel.viewModel
import dev.arunkumar.android.logging.logD
import dev.arunkumar.android.ui.theme.BaseTheme
import javax.inject.Inject

class HomeActivity : DaggerAppCompatActivity(), UsesViewModel {
  @Inject
  override lateinit var viewModelFactory: ViewModelProvider.Factory

  private val homeViewModel by viewModel<HomeViewModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      BaseTheme {
        Text(text = "Hello World")
      }
    }
  }

  private fun render(homeState: HomeState) {
    logD { homeState.toString() }
  }

  /*private fun deleteItem(homeSideEffect: HomeSideEffect.PerformDelete) {
    val workRequest = OneTimeWorkRequestBuilder<DeleteItemWorker>().run {
      setInputData(workDataOf("id" to homeSideEffect.item.id))
      build()
    }
    workManager.enqueue(workRequest)
  }*/

  @Module
  interface HomeModule {
    @PerActivity
    @Binds
    fun HomeActivity.bindActivity(): Activity
  }

  @Module
  interface Builder {
    @PerActivity
    @ContributesAndroidInjector(modules = [HomeModule::class])
    fun homeActivity(): HomeActivity

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    fun HomeViewModel.homeViewModel(): ViewModel
  }
}
