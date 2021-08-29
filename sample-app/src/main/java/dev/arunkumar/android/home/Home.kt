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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import dev.arunkumar.android.home.tasks.Tasks

@Composable
fun Home(state: HomeState, homeViewModel: HomeViewModel) {
  val scaffoldState = rememberScaffoldState()
  val scope = rememberCoroutineScope()
  Scaffold(
    scaffoldState = scaffoldState,
    topBar = {
      TopAppBar {
        Box {
          Text(
            text = "Tasks",
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
          )
        }
      }
    },
    bottomBar = {
      BottomAppBar {

      }
    },
    isFloatingActionButtonDocked = true,
    floatingActionButton = {
      ExtendedFloatingActionButton(
        text = { Text("Create Task") },
        onClick = { homeViewModel.perform(HomeAction.AddTask("Hello")) },
        icon = {
          Icon(Icons.Default.Add, contentDescription = null)
        }
      )
    },
    floatingActionButtonPosition = FabPosition.Center,
    content = { innerPadding ->
      Tasks(state.tasks, innerPadding)
    }
  )
}
