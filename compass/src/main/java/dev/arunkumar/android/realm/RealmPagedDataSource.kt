package dev.arunkumar.android.realm

import androidx.paging.DataSource
import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmQuery
import io.realm.RealmResults

class RealmPagedDataSource<T : RealmModel>(
  realmQueryBuilder: (Realm) -> RealmQuery<T>
) : TiledDataSource<T>() {

  class Factory<T : RealmModel>(
    private val realmQueryBuilder: (Realm) -> RealmQuery<T>
  ) : DataSource.Factory<Int, T>() {
    override fun create() = RealmPagedDataSource(realmQueryBuilder)
  }

  private var realm: Realm = defaultRealm()
  private var realmResults: RealmResults<T> = realmQueryBuilder(realm).findAll().apply {
    addChangeListener { _ ->
      this@RealmPagedDataSource.realm.close()
      invalidate()
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