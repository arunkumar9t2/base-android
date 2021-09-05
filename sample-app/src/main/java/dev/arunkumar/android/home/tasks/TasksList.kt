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

@file:Suppress("NOTHING_TO_INLINE")

package dev.arunkumar.android.home.tasks

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Tonality
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import dev.arunkumar.android.tasks.data.Task
import dev.arunkumar.android.util.RealmItem
import io.realm.kotlin.where
import kotlinx.coroutines.flow.Flow
import java.util.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TasksList(
  tasks: Flow<PagingData<Task>>,
  contentPadding: PaddingValues,
  taskContent: @Composable (task: Task?) -> Unit,
  modifier: Modifier = Modifier,
) {
  val items = tasks.collectAsLazyPagingItems()
  LazyColumn(
    modifier = modifier,
    contentPadding = contentPadding
  ) {
    items(items) { task -> taskContent(task) }
  }
}

@Composable
fun TaskItem(
  task: Task,
  deleteTask: (taskId: UUID) -> Unit,
  completeTask: (taskId: UUID, completed: Boolean) -> Unit,
  modifier: Modifier = Modifier
) {
  Card(modifier = modifier
    .fillMaxWidth()
    .clickable { deleteTask(task.id) }
  ) {
    Row(modifier = Modifier.padding(12.dp)) {
      Checkbox(
        checked = task.completed,
        onCheckedChange = { completed -> completeTask(task.id, completed) },
        modifier = Modifier
          .size(24.dp)
          .align(Alignment.Top)
      )
      Spacer(modifier = Modifier.width(12.dp))
      Column {
        Text(
          text = task.name,
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
        TaskMetaRow {
          TaskTimeSpent(howMuch = "40m")
          TaskProgress(progress = 60)
        }
        Tags(
          listOf(
            "learning",
            "projects",
            "iconzy",
            "jarvis",
          )
        )
      }
    }
  }
}

@Composable
private fun Tags(tags: List<String>, modifier: Modifier = Modifier) {
  LazyRow(
    modifier = modifier
      .fillMaxWidth()
      .padding(2.dp)
  ) {
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
private fun TaskMetaRow(
  modifier: Modifier = Modifier,
  block: @Composable RowScope.() -> Unit
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier
      .alpha(0.85F)
      .padding(4.dp)
  ) {
    block()
  }
}

@Composable
private fun TaskMeta(
  icon: ImageVector,
  text: String,
  modifier: Modifier = Modifier
) {
  Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier.padding(2.dp)) {
    Icon(icon, "", modifier = Modifier.size(12.dp))
    Spacer(modifier = Modifier.width(2.dp))
    Text(
      text = text,
      style = MaterialTheme.typography.caption.copy(fontSize = 10.sp),
    )
  }
}

@Composable
private inline fun TaskTimeSpent(howMuch: String) {
  TaskMeta(icon = Icons.Filled.Alarm, text = howMuch)
}

@Composable
private inline fun TaskProgress(progress: Int) {
  TaskMeta(icon = Icons.Filled.Tonality, text = "$progress%")
}


@Composable
fun RowScope.DemoTaskModel() {
  RealmItem<Task>(
    queryBuilder = { where<Task>().sort("name").limit(1) },
    content = { task ->
      if (task != null) {
        val taskColor by animateColorAsState(
          targetValue = if (task.completed) Color.Green else Color.White
        )
        val textDecoration = if (task.completed) TextDecoration.LineThrough else null
        Text(
          text = task.name,
          color = taskColor,
          textDecoration = textDecoration,
          modifier = Modifier.weight(1.0F)
        )
      }
    }
  )
}
