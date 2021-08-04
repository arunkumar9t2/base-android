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

package dev.arunkumar.android.realm

import dev.arunkumar.android.realm.threading.RealmSchedulers
import dev.arunkumar.android.rx.createObservable
import io.reactivex.Observable
import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmQuery

typealias RealmBlock = (Realm) -> Unit
typealias RealmFunction<T> = (Realm) -> T

inline fun defaultRealm(): Realm = Realm.getDefaultInstance()

inline fun realm(action: RealmBlock) {
  val realm = defaultRealm()
  action(realm)
  realm.close()
}

inline fun <T> realmFunction(block: RealmFunction<T>): T {
  val realm = defaultRealm()
  return block(realm).also { realm.close() }
}

inline fun realmTransaction(noinline action: RealmBlock) {
  val realm = defaultRealm()
  realm.executeTransaction(action)
  realm.close()
}

fun <T : RealmModel> realmObservable(
  tag: String? = null,
  realmQuery: (Realm) -> RealmQuery<T>
): Observable<List<T>> {
  return createObservable<List<T>> {
    try {
      val realm = defaultRealm()
      val results = realmQuery(realm).findAll()
      onNext(realm.copyFromRealm(results))
      results.addChangeListener { newResults -> onNext(realm.copyFromRealm(newResults)) }
      setCancellable {
        results.removeAllChangeListeners()
        realm.close()
      }
    } catch (e: Exception) {
      tryOnError(e)
    }
  }.compose(RealmSchedulers.apply(tag))
}
