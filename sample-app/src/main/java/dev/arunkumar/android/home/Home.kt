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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.arunkumar.android.home.tasks.DemoTaskModel
import dev.arunkumar.android.home.tasks.TaskItem
import dev.arunkumar.android.home.tasks.TasksList
import dev.arunkumar.android.util.invoke
import dev.arunkumar.common.result.Resource

@Composable
fun Home(state: HomeState, homeViewModel: HomeViewModel) {
  val scaffoldState = rememberScaffoldState()
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
      TasksBottomBar(state.reset) { homeViewModel.perform(HomeAction.ResetTasks) }
    },
    isFloatingActionButtonDocked = false,
    floatingActionButton = {
      FloatingActionButton(
        onClick = { homeViewModel.perform(HomeAction.AddTask("Hello")) },
        content = {
          Icon(Icons.Default.Add, contentDescription = null)
        }
      )
    },
    floatingActionButtonPosition = FabPosition.End,
    content = { innerPadding ->
      TasksList(state.tasks, innerPadding, taskContent = { task ->
        if (task != null) {
          TaskItem(
            task = task,
            deleteTask = {
              homeViewModel.perform(HomeAction.DeleteTask(it))
            },
            completeTask = { taskId, completed ->
              homeViewModel.perform(HomeAction.CompleteTask(taskId, completed))
            }
          )
        }
      })
    }
  )
}

@Composable
private fun TasksBottomBar(resetState: Resource<Unit>, resetAll: () -> Unit) {
  BottomAppBar {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.End,
      modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)
    ) {
      // Render a single item directly
      DemoTaskModel()
      resetState(
        loading = {
          CircularProgressIndicator()
        },
        success = {
          Icon(
            imageVector = Icons.Filled.ClearAll,
            contentDescription = "Clear All",
            modifier = Modifier.clickable { resetAll() }
          )
        }
      )
    }
  }
}
