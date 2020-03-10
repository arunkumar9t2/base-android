package dev.arunkumar.android.home

import androidx.paging.PagedList
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.jakewharton.rxrelay2.BehaviorRelay
import dev.arunkumar.android.data.DefaultItemsRepository
import dev.arunkumar.android.data.DeleteItemWorker
import dev.arunkumar.android.data.Item
import dev.arunkumar.android.rxschedulers.SchedulerProvider
import dev.arunkumar.android.viewmodel.RxViewModel
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class HomeViewModel
@Inject
constructor(
    private val schedulerProvider: SchedulerProvider,
    private val defaultItemsRepository: DefaultItemsRepository,
    private val workManager: WorkManager
) : RxViewModel() {

    val itemsPagedList = BehaviorRelay.create<PagedList<Item>>()

    init {
        start()
    }

    private fun start() {
        defaultItemsRepository.items()
            .observeOn(schedulerProvider.ui)
            .untilCleared()
            .subscribeBy(onNext = itemsPagedList::accept)
    }

    fun delete(item: Item) {
        //TODO Dereference work manager from here.
        val workRequest = OneTimeWorkRequestBuilder<DeleteItemWorker>().run {
            setInputData(workDataOf("id" to item.id))
            build()
        }
        workManager.enqueue(workRequest)
    }
}