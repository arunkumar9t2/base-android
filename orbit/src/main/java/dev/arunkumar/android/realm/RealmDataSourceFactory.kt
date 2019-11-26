package dev.arunkumar.android.realm

import androidx.paging.DataSource
import dev.arunkumar.android.realm.threading.RealmExecutor
import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmQuery

class RealmDataSourceFactory<T : RealmModel>(
    private val realmExecutor: RealmExecutor,
    private val realmQueryBuilder: (Realm) -> RealmQuery<T>
) : DataSource.Factory<Int, T>() {
    override fun create() = RealmPagedDataSource(realmQueryBuilder).apply {
        addInvalidatedCallback {
            realmExecutor.stop()
        }
    }
}