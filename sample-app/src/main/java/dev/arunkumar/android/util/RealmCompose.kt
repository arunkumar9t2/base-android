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

package dev.arunkumar.android.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import dev.arunkumar.android.realm.RealmQueryBuilder
import dev.arunkumar.android.realm.flow.asFlow
import dev.arunkumar.android.util.work.rememberFlowWithLifecycle
import io.realm.RealmModel
import kotlinx.coroutines.flow.onCompletion

@Composable
inline fun <reified T : RealmModel> RealmItem(
  noinline realmQueryBuilder: RealmQueryBuilder<T>,
  content: @Composable (value: T) -> Unit
) {
  val value by rememberFlowWithLifecycle(flow = realmQueryBuilder.asFlow())
    .onCompletion { printThread("Completed") }
    .collectAsState(initial = null)
  key(value) {
    value?.firstOrNull()?.let { realmValue -> content(realmValue) }
  }
}