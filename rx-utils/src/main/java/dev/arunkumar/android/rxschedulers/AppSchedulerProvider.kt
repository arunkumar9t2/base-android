package dev.arunkumar.android.rxschedulers

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AppSchedulerProvider @Inject constructor() : SchedulerProvider {
    override val ui: Scheduler get() = AndroidSchedulers.mainThread()
    override val single: Scheduler get() = Schedulers.single()
    override val io: Scheduler get() = Schedulers.io()
    override val pool: Scheduler get() = Schedulers.computation()
    override val new: Scheduler get() = Schedulers.newThread()
}