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

@file:Suppress("NOTHING_TO_INLINE")

package dev.arunkumar.android.realm

import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmQuery

typealias RealmBlock = (Realm) -> Unit
typealias RealmFunction<T> = (Realm) -> T
typealias RealmQueryBuilder<T> = Realm.() -> RealmQuery<T>

@Suppress("FunctionName")
inline fun DefaultRealm(): Realm = Realm.getDefaultInstance()

inline fun realm(action: RealmBlock) {
  val realm = DefaultRealm()
  action(realm)
  realm.close()
}

@Suppress("FunctionName")
inline fun <T> RealmFunction(block: RealmFunction<T>): T {
  val realm = DefaultRealm()
  return block(realm).also { realm.close() }
}

@Suppress("FunctionName")
inline fun RealmTransaction(noinline action: RealmBlock) {
  val realm = DefaultRealm()
  realm.executeTransaction(action)
  realm.close()
}

@Suppress("FunctionName")
fun <T : RealmModel> RealmQuery(
  builder: RealmQueryBuilder<T>
): RealmQueryBuilder<T> = builder