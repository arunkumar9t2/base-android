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

package dev.arunkumar.android.realm.paging

import androidx.paging.DataSource
import dev.arunkumar.android.realm.defaultRealm
import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmQuery
import io.realm.RealmResults

class RealmTiledDataSource<T : RealmModel>(
  realmQueryBuilder: (Realm) -> RealmQuery<T>
) : TiledDataSource<T>() {

  class Factory<T : RealmModel>(
    private val realmQueryBuilder: (Realm) -> RealmQuery<T>
  ) : DataSource.Factory<Int, T>() {
    override fun create() = RealmTiledDataSource(realmQueryBuilder)
  }

  private var realm: Realm = defaultRealm()

  private var realmResults: RealmResults<T> = realmQueryBuilder(realm).findAll().apply {
    addChangeListener { _ -> invalidate() }
  }

  init {
    addInvalidatedCallback {
      if (realmResults.isValid) {
        realmResults.removeAllChangeListeners()
      }
      this@RealmTiledDataSource.realm.close()
    }
  }

  override fun countItems() = when {
    realm.isClosed || !realmResults.isValid -> 0
    else -> realmResults.size
  }

  override fun loadRange(startPosition: Int, count: Int): List<T> {
    if (countItems() == 0) return emptyList()
    val results = mutableListOf<T>()
    val endPosition = minOf(startPosition + count, countItems())
    for (position in startPosition until endPosition) {
      realmResults[position]?.let { item ->
        results.add(realm.copyFromRealm(item))
      }
    }
    return results.toList()
  }
}
