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

package dev.arunkumar.android.transformers

import io.reactivex.*
import org.reactivestreams.Publisher

interface CompositeTransformer<Type> :
  FlowableTransformer<Type, Type>,
  SingleTransformer<Type, Type>,
  CompletableTransformer,
  ObservableTransformer<Type, Type>

class DefaultCompositeTransformer<Type>
constructor(
  private val upstreamScheduler: Scheduler,
  private val downstreamScheduler: Scheduler
) : CompositeTransformer<Type> {

  override fun apply(upstream: Flowable<Type>): Publisher<Type> {
    return upstream.subscribeOn(upstreamScheduler).observeOn(downstreamScheduler)
  }

  override fun apply(upstream: Single<Type>): SingleSource<Type> {
    return upstream.subscribeOn(upstreamScheduler).observeOn(downstreamScheduler)
  }

  override fun apply(upstream: Completable): CompletableSource {
    return upstream.subscribeOn(upstreamScheduler).observeOn(downstreamScheduler)
  }

  override fun apply(upstream: Observable<Type>): ObservableSource<Type> {
    return upstream.subscribeOn(upstreamScheduler).observeOn(downstreamScheduler)
  }
}