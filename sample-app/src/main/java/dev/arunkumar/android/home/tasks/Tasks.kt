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

package dev.arunkumar.android.home.tasks

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.arunkumar.android.home.Task
import dev.arunkumar.common.result.Resource
import dev.arunkumar.common.result.on

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Tasks(
  tasks: Resource<List<Task>>,
  contentPadding: PaddingValues,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier,
    contentPadding = contentPadding
  ) {
    tasks.on(
      success = {
        items(data) { task ->
          Task(task)
        }
      },
      loading = {
        item("loading") {
          Text(text = "Loading")
        }
      },
      error = {
        item("loading") {
          Text(text = "Error")
        }
      }
    )
  }
}

@Composable
fun Task(task: Task) {
  Card(modifier = Modifier.fillMaxWidth()) {
    Row(modifier = Modifier.padding(12.dp)) {
      Checkbox(
        checked = false,
        onCheckedChange = {},
        modifier = Modifier
          .size(24.dp)
          .align(Alignment.Top)
      )
      Spacer(modifier = Modifier.width(12.dp))
      Column {
        Text(
          text = task,
          style = MaterialTheme.typography.body1
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Early access to Jarvis",
          style = MaterialTheme.typography.caption.copy(
            fontSize = 10.sp,
            color = contentColorFor(backgroundColor = MaterialTheme.colors.onSurface).copy(alpha = 0.7f)
          )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Tags(listOf("learning", "projects", "iconzy", "jarvis"))
        TaskMeta(modifier = Modifier.alpha(0.85F))
        Spacer(modifier = Modifier.height(4.dp))
      }
    }
  }
}

@Composable
private fun Tags(tags: List<String>) {
  LazyRow(modifier = Modifier.fillMaxWidth()) {
    itemsIndexed(tags) { _, tag ->
      Text(
        text = "  $tag  ",
        style = MaterialTheme.typography.caption.copy(fontSize = 10.sp),
        modifier = Modifier
          .background(Color(0xFF64b5f2), RoundedCornerShape(size = 16.dp))
          .padding(2.dp)
      )
      Spacer(modifier = Modifier.width(4.dp))
    }
  }
}

@Composable
private fun TaskMeta(modifier: Modifier = Modifier) {
  Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier.padding(4.dp)) {
    TimeSpent(howMuch = "50m")
  }
}

@Composable
private fun TimeSpent(howMuch: String) {
  Row(verticalAlignment = Alignment.CenterVertically) {
    Icon(Icons.Filled.Alarm, "", modifier = Modifier.size(12.dp))
    Spacer(modifier = Modifier.width(2.dp))
    Text(
      text = howMuch,
      style = MaterialTheme.typography.caption.copy(fontSize = 8.sp),
    )
  }
}
