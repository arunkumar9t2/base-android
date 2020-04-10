package dev.arunkumar.android.realm.threading

import dev.arunkumar.android.rxschedulers.toScheduler
import io.reactivex.Scheduler

object RealmSchedulers {
  fun realmScheduler(tag: String? = null): Scheduler {
    return RealmExecutor(tag).toScheduler()
  }
}