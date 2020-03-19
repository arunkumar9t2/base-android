package dev.arunkumar.android.realm

import androidx.paging.DataSource
import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmQuery

class RealmDataSourceFactory<T : RealmModel>(
  private val realmQueryBuilder: (Realm) -> RealmQuery<T>
) : DataSource.Factory<Int, T>() {
  override fun create() = RealmPagedDataSource(realmQueryBuilder)
}