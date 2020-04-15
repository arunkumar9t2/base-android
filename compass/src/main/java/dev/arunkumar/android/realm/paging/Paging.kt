package dev.arunkumar.android.realm.paging

import androidx.paging.PagedList

fun pagingConfig(block: PagedList.Config.Builder.() -> Unit): PagedList.Config {
  return PagedList.Config.Builder().apply(block).build()
}