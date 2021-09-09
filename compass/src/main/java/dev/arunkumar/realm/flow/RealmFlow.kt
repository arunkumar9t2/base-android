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

@file:Suppress("FunctionName")

package dev.arunkumar.realm.flow

import dev.arunkumar.realm.DefaultRealm
import dev.arunkumar.realm.RealmQueryBuilder
import dev.arunkumar.realm.threading.RealmDispatcher
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmModel
import io.realm.RealmResults
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive

private typealias RealmModelTransform<T, R> = Realm.(realmModel: T) -> R

private fun <T : RealmModel, R> RealmCopyTransform(): RealmModelTransform<T, R> {
  return { model -> copyFromRealm(model) as R }
}
private typealias RealmDispatcherProvider = () -> RealmDispatcher

fun <T : RealmModel> RealmQueryBuilder<T>.asFlow(
  dispatcherProvider: RealmDispatcherProvider = { RealmDispatcher() },
): Flow<List<T>> = asFlow(
  dispatcherProvider = dispatcherProvider,
  transform = RealmCopyTransform()
)

fun <T : RealmModel, R> RealmQueryBuilder<T>.asFlow(
  dispatcherProvider: RealmDispatcherProvider = { RealmDispatcher() },
  transform: RealmModelTransform<T, R> = RealmCopyTransform()
): Flow<List<R>> {
  return flow {
    emit(dispatcherProvider())
  }.flatMapConcat { realmDispatcher: RealmDispatcher ->
    callbackFlow {
      val realm = DefaultRealm()
      val realmQuery = this@asFlow(realm)
      val results = realmQuery.findAll()

      if (!results.isValid) {
        awaitClose {}
        realm.close()
        return@callbackFlow
      }

      fun RealmResults<T>.transform(): List<R> = map { value -> realm.transform(value) }

      // Emit initial result
      offer(results.transform())

      val realmChangeListener = RealmChangeListener<RealmResults<T>> { listenerResults ->
        if (isActive) {
          // Emit any changes
          offer(listenerResults.transform())
        }
      }
      results.addChangeListener(realmChangeListener)
      awaitClose {
        results.removeChangeListener(realmChangeListener)
        realm.close()
      }
    }.flowOn(realmDispatcher).onCompletion { realmDispatcher.stop() }
  }
}
