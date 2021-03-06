package dev.arunkumar.android.realm.paging

import android.util.Log
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import dev.arunkumar.android.realm.defaultRealm
import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmQuery
import io.realm.RealmResults

class RealmPagedDataSource<T : RealmModel>(
  private val realmQueryBuilder: (Realm) -> RealmQuery<T>
) : PageKeyedDataSource<Int, T>() {

  class Factory<T : RealmModel>(
    private val realmQueryBuilder: (Realm) -> RealmQuery<T>
  ) : DataSource.Factory<Int, T>() {
    override fun create() = RealmPagedDataSource(realmQueryBuilder)
  }

  private var realm: Realm = defaultRealm()
  private var realmChangeListener = { _: RealmResults<T> ->
    invalidate()
  }

  init {
    addInvalidatedCallback {
      this@RealmPagedDataSource.realm.close()
    }
  }

  override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, T>) {
    loadPage(pageNo = 1, loadSize = params.requestedLoadSize) { results ->
      callback.onResult(results, null, 2)
    }
  }

  override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
    loadPage(pageNo = params.key, loadSize = params.requestedLoadSize) { result ->
      callback.onResult(result, params.key + 1)
    }
  }

  override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
    loadPage(pageNo = params.key, loadSize = params.requestedLoadSize) { result ->
      callback.onResult(result, params.key - 1)
    }
  }

  private fun loadPage(pageNo: Int, loadSize: Int, onLoaded: (List<T>) -> Unit) {
    val realmResults = realmQueryBuilder(realm).findAll().apply {
      addChangeListener(realmChangeListener)
    }
    val startPosition = (pageNo - 1) * loadSize
    val endPosition = minOf(startPosition + loadSize, realmResults.size)
    Log.d("Paged", "loadPage: $pageNo, $loadSize, $startPosition, $endPosition")
    val copiedResults = mutableListOf<T>()
    for (position in startPosition until endPosition) {
      realmResults[position]?.let { item ->
        copiedResults.add(realm.copyFromRealm(item))
      }
    }
    onLoaded(copiedResults.toList())
  }
}