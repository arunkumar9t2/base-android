@file:Suppress("NOTHING_TO_INLINE")

package dev.arunkumar.android.realm

import io.realm.Realm

typealias RealmBlock = (Realm) -> Unit
typealias RealmFunction<T> = (Realm) -> T

inline fun defaultRealm(): Realm = Realm.getDefaultInstance()

inline fun realm(action: RealmBlock) {
    val realm = defaultRealm()
    action(realm)
    realm.close()
}

inline fun <T> realmFunction(block: RealmFunction<T>): T {
    val realm = defaultRealm()
    return block(realm).also { realm.close() }
}

inline fun realmTransaction(noinline action: RealmBlock) {
    val realm = defaultRealm()
    realm.executeTransaction(action)
    realm.close()
}
