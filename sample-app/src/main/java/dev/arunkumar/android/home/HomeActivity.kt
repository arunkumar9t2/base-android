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

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerAppCompatActivity
import dev.arunkumar.android.dagger.viewmodel.UsesViewModel
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
          .collectAsState(initial = homeViewModel.state.value)
        Home(state, homeViewModel)
      }
    }
  }
}
