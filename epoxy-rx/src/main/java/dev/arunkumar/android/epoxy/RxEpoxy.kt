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

package dev.arunkumar.android.epoxy

import androidx.recyclerview.widget.ListUpdateCallback
import com.airbnb.epoxy.*
import dev.arunkumar.android.rxschedulers.toScheduler
import io.reactivex.Observable

fun EpoxyController.buildEvents(): Observable<DiffResult> = Observable.create { emitter ->
  val buildListener = OnModelBuildFinishedListener { result: DiffResult ->
    emitter.onNext(result)
  }.also(::addModelBuildListener)
  emitter.setCancellable { removeModelBuildListener(buildListener) }
}

fun EpoxyController.intercepts(): Observable<List<EpoxyModel<*>>> = Observable.create { emitter ->
  val interceptor = EpoxyController.Interceptor { models ->
    emitter.onNext(models)
  }.also(::addInterceptor)
  emitter.setCancellable { removeInterceptor(interceptor) }
}

fun DiffResult.on(
  changedCallback: (Int, Int, Any?) -> Unit = { _, _, _ -> },
  movedCallback: (Int, Int) -> Unit = { _, _ -> },
  insertedCallback: (Int, Int) -> Unit = { _, _ -> },
  removedCallback: (Int, Int) -> Unit = { _, _ -> }
) {
  dispatchTo(object : ListUpdateCallback {
    override fun onChanged(position: Int, count: Int, payload: Any?) {
      changedCallback(position, count, payload)
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
      movedCallback(fromPosition, toPosition)
    }

    override fun onInserted(position: Int, count: Int) {
      insertedCallback(position, count)
    }

    override fun onRemoved(position: Int, count: Int) {
      removedCallback(position, count)
    }
  })
}

@Suppress("NOTHING_TO_INLINE")
inline fun epoxyAsyncScheduler() = EpoxyAsyncUtil.getAsyncBackgroundHandler().toScheduler()