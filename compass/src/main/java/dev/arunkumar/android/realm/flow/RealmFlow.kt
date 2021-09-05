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

package dev.arunkumar.android.realm.flow

import dev.arunkumar.android.realm.DefaultRealm
import dev.arunkumar.android.realm.RealmQueryBuilder
import dev.arunkumar.android.realm.threading.RealmDispatcher
import io.realm.RealmChangeListener
import io.realm.RealmModel
import io.realm.RealmResults
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive

fun <T : RealmModel> RealmQueryBuilder<T>.asFlow(
  dispatcherProvider: () -> RealmDispatcher = { RealmDispatcher() }
): Flow<List<T>> = flow {
  emit(dispatcherProvider())
}.flatMapConcat { realmDispatcher: RealmDispatcher ->
  callbackFlow {
    val realm = DefaultRealm()
    val realmQuery = this@asFlow(realm)
    val results = realmQuery.findAll()
    if (!results.isValid) {
      awaitClose {}
      return@callbackFlow
    }
    fun RealmResults<T>.copy(): List<T> = realm.copyFromRealm(this)
    // Emit initial result
    this.offer(results.copy())
    val realmChangeListener = RealmChangeListener<RealmResults<T>> { listenerResults ->
      if (isActive) {
        // Emit any changes
        this.offer(listenerResults.copy())
      }
    }
    results.addChangeListener(realmChangeListener)
    awaitClose {
      results.removeChangeListener(realmChangeListener)
      realm.close()
    }
  }.flowOn(realmDispatcher)
    .onCompletion { realmDispatcher.stop() }
}
