package dev.arunkumar.android.home

import com.jakewharton.rxrelay2.BehaviorRelay
import dev.arunkumar.android.viewmodel.RxViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HomeViewModel
@Inject
constructor() : RxViewModel() {

    val count = BehaviorRelay.create<Long>()

    init {
        start()
    }

    private fun start() {
        Observable.interval(100, TimeUnit.MILLISECONDS)
            .untilCleared()
            .doOnNext { logd(it.toString()) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = count::accept)
    }
}