package dev.arunkumar.android.rxschedulers

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TestSchedulerProvider
@Inject constructor() : SchedulerProvider {
    override val ui: Scheduler get() = Schedulers.trampoline()
    override val single: Scheduler get() = Schedulers.trampoline()
    override val io: Scheduler get() = Schedulers.trampoline()
    override val pool: Scheduler get() = Schedulers.trampoline()
    override val new: Scheduler get() = Schedulers.trampoline()
}