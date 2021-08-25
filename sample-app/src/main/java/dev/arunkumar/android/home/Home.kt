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

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.arunkumar.common.result.Resource

@Composable
fun Home(state: HomeState, homeViewModel: HomeViewModel) {
  Column(
    modifier = Modifier.padding(16.dp)
  ) {
    Tasks(state.tasks, modifier = Modifier.weight(1F))
    Button(
      onClick = {
        homeViewModel.perform(HomeAction.AddTask("Hello"))
      },
      modifier = Modifier.fillMaxWidth()
    ) {
      Text(text = "Click Me")
    }
  }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Tasks(tasks: Resource<List<Task>>, modifier: Modifier) {
  LazyColumn(modifier = modifier) {
    when (tasks) {
      is Resource.Success -> {
        items(tasks.data) { task ->
          Text(text = task)
        }
      }
      is Resource.Loading -> {
        item("loading") {
          Text(text = "Loading")
        }
      }
      is Resource.Error -> {
        item("loading") {
          Text(text = "Error")
        }
      }
      else -> {
      }
    }
  }
}
