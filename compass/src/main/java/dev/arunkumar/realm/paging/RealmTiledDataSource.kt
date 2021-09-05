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

package dev.arunkumar.realm.paging

import androidx.paging.DataSource
import dev.arunkumar.realm.DefaultRealm
import dev.arunkumar.realm.RealmQueryBuilder
import io.realm.RealmModel
import io.realm.RealmResults

class RealmTiledDataSource<T : RealmModel>(
  private val realmQueryBuilder: RealmQueryBuilder<T>
) : TiledDataSource<T>() {

  class Factory<T : RealmModel>(
    private val realmQueryBuilder: RealmQueryBuilder<T>
  ) : DataSource.Factory<Int, T>() {
    override fun create() = RealmTiledDataSource(realmQueryBuilder)
  }

  private val realm by lazy { DefaultRealm() }
  private val realmQuery by lazy { realm.realmQueryBuilder() }
  private val realmChangeListener = { _: RealmResults<T> -> invalidate() }
  private val realmResults by lazy {
    realmQuery.findAll().apply {
      addChangeListener(realmChangeListener)
    }
  }

  init {
    addInvalidatedCallback {
      if (realmResults.isValid) {
        realmResults.removeChangeListener(realmChangeListener)
      }
      realm.close()
    }
  }

  override fun countItems() = when {
    realm.isClosed || !realmResults.isValid -> 0
    else -> realmResults.size
  }

  override fun loadRange(startPosition: Int, count: Int): List<T> {
    val size = countItems()
    if (size == 0) return emptyList()
    return buildList {
      val endPosition = minOf(startPosition + count, size)
      for (position in startPosition until endPosition) {
        realmResults[position]?.let { item ->
          add(realm.copyFromRealm(item))
        }
      }
    }
  }
}
