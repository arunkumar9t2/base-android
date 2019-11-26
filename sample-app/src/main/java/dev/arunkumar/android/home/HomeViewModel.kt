package dev.arunkumar.android.home

import androidx.paging.PagedList
import com.jakewharton.rxrelay2.BehaviorRelay
import dev.arunkumar.android.data.DefaultItemsRepository
import dev.arunkumar.android.data.Item
import dev.arunkumar.android.rxschedulers.SchedulerProvider
import dev.arunkumar.android.viewmodel.RxViewModel
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class HomeViewModel
@Inject
constructor(
    private val schedulerProvider: SchedulerProvider,
    private val defaultItemsRepository: DefaultItemsRepository
) : RxViewModel() {

    val itemsPagedList = BehaviorRelay.create<PagedList<Item>>()

    init {
        start()
    }

    private fun start() {
        defaultItemsRepository.items()
            .observeOn(schedulerProvider.ui)
            .subscribeBy(onNext = itemsPagedList::accept)
    }
}