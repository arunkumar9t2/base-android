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

package dev.arunkumar.realm.rx

import dev.arunkumar.android.rx.createObservable
import dev.arunkumar.realm.DefaultRealm
import dev.arunkumar.realm.RealmQueryBuilder
import dev.arunkumar.realm.threading.RealmSchedulers
import io.reactivex.Observable
import io.realm.RealmChangeListener
import io.realm.RealmModel
import io.realm.RealmResults

fun <T : RealmModel> T.asObservable(
  tag: String? = null,
  realmQuery: RealmQueryBuilder<T>
): Observable<List<T>> {
  return createObservable<List<T>> {
    try {
      val realm = DefaultRealm()
      val results = realmQuery(realm).findAll()
      fun RealmResults<T>.copy(): List<T> = realm.copyFromRealm(this)
      // Emit initial results
      onNext(results.copy())
      val listener = RealmChangeListener<RealmResults<T>> { listenerResults ->
        // Emit changed results
        onNext(listenerResults.copy())
      }
      results.addChangeListener(listener)

      setCancellable {
        results.removeChangeListener(listener)
        realm.close()
      }
    } catch (e: Exception) {
      tryOnError(e)
    }
  }.compose(RealmSchedulers.create(tag))
}
