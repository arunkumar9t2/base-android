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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import dev.arunkumar.android.ui.theme.BaseTheme
import dev.arunkumar.android.util.work.rememberFlowWithLifecycle
import javax.inject.Inject

class HomeActivity : DaggerAppCompatActivity(), UsesViewModel {
  @Inject
  override lateinit var viewModelFactory: ViewModelProvider.Factory

  private val homeViewModel by viewModel<HomeViewModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      BaseTheme {
        val state by rememberFlowWithLifecycle(flow = homeViewModel.state)
          .collectAsState(initial = HomeState())
        Box(
          modifier = Modifier.fillMaxSize(),
          contentAlignment = Alignment.Center,
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(state.toolbar)
            Button(onClick = {
              homeViewModel.perform(HomeAction.LoadItems)
            }) {
              Text(text = "Click Me")
            }
          }
        }
      }
    }
  }

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
