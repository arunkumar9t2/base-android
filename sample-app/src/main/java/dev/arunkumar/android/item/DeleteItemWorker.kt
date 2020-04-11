package dev.arunkumar.android.item

import android.app.Application
import androidx.work.ListenableWorker
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import dev.arunkumar.android.dagger.workmanager.WorkerKey
import dev.arunkumar.android.logging.logd
import io.reactivex.Single
import javax.inject.Inject

class DeleteItemWorker
@Inject
constructor(
  private val itemsRepository: ItemsRepository,
  application: Application,
  private val workerParams: WorkerParameters
) : RxWorker(application, workerParams) {

  override fun createWork(): Single<Result> {
    val id = workerParams.inputData.keyValueMap["id"] as Int
    return itemsRepository
      .deleteItem(id)
      .toSingle { Result.success() }
      .doOnSuccess {
        logd("Delete item id $id success")
      }
  }

  @Module
  interface Builder {
    @Binds
    @IntoMap
    @WorkerKey(DeleteItemWorker::class)
    fun bindWorker(deleteItemWorker: DeleteItemWorker): ListenableWorker
  }
}