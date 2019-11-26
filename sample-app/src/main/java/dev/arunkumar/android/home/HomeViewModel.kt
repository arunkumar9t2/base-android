package dev.arunkumar.android.home

import com.jakewharton.rxrelay2.BehaviorRelay
import dev.arunkumar.android.home.items.DefaultItemsRepository
import dev.arunkumar.android.logging.logd
import dev.arunkumar.android.rxschedulers.SchedulerProvider
import dev.arunkumar.android.viewmodel.RxViewModel
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.inject.Inject

class HomeViewModel
@Inject
constructor(
    private val schedulerProvider: SchedulerProvider,
    private val defaultItemsRepository: DefaultItemsRepository
) : RxViewModel() {

    val count = BehaviorRelay.create<Long>()

    init {
        start()
    }

    private fun start() {
        Observable.interval(500, MILLISECONDS, schedulerProvider.pool)
            .doOnNext { logd(it.toString()) }
            .observeOn(schedulerProvider.ui)
            .untilCleared()
            .subscribeBy(onNext = count::accept)
    }
}