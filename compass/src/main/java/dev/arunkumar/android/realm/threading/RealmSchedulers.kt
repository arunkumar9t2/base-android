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

package dev.arunkumar.android.realm.threading

import dev.arunkumar.android.rxschedulers.toScheduler
import dev.arunkumar.android.transformers.CompositeTransformer
import io.reactivex.*

object RealmSchedulers {

  class RealmBgCompositeTransformer<T>(
    private val tag: String? = null
  ) : CompositeTransformer<T> {

    private fun createRealmExecutor(): RealmExecutor = RealmExecutor(tag)

    override fun apply(upstream: Flowable<T>): Flowable<T> = createRealmExecutor().let {
      val scheduler = it.toScheduler()
      upstream.subscribeOn(scheduler)
        .doAfterTerminate { it.stop() }
        .unsubscribeOn(scheduler)
    }

    override fun apply(upstream: Single<T>): SingleSource<T> = createRealmExecutor().let {
      val scheduler = it.toScheduler()
      upstream.subscribeOn(scheduler)
        .doAfterTerminate { it.stop() }
        .unsubscribeOn(scheduler)
    }

    override fun apply(upstream: Completable): CompletableSource = createRealmExecutor().let {
      val scheduler = it.toScheduler()
      upstream.subscribeOn(scheduler)
        .doAfterTerminate { it.stop() }
        .unsubscribeOn(scheduler)
    }

    override fun apply(upstream: Observable<T>): ObservableSource<T> = createRealmExecutor().let {
      val scheduler = it.toScheduler()
      upstream.subscribeOn(scheduler)
        .doAfterTerminate { it.stop() }
        .unsubscribeOn(scheduler)
    }
  }

  fun <T> create(tag: String? = null): CompositeTransformer<T> = RealmBgCompositeTransformer(tag)
}
