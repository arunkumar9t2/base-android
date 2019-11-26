package dev.arunkumar.android.realm

import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmResults

class RealmPagedDataSource<T : RealmModel>(
    private val realm: Realm,
    private val realmResults: RealmResults<T>
) : TiledDataSource<T>() {

    init {
        realmResults.addChangeListener { results ->
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
        val endPosition = startPosition + count
        for (position in startPosition until endPosition) {
            realmResults[position]?.let { item ->
                results.add(realm.copyFromRealm(item))
            }
        }
        return results.toList()
    }
}