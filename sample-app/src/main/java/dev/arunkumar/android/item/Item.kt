package dev.arunkumar.android.item

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Item(
  @PrimaryKey
  var id: Int = 0,
  var name: String = ""
) : RealmObject()