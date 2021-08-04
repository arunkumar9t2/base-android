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

package dev.arunkumar.android.viewmodel

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.BackpressureStrategy.LATEST
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

abstract class RxViewModel : ViewModel() {
  /**
   * Relay to convert `onCleared` calls to data stream.
   *
   * @see onCleared
   */
  private val clearEventsSubject = PublishRelay.create<Int>()

  /**
   * [Observable] that emits `0` when onCleared is called.
   */
  @Suppress("MemberVisibilityCanBePrivate")
  protected val clearEvents: Observable<Int> = clearEventsSubject.hide()

  /**
   * Auto terminates the current [Observable] when `onCleared` occurs.
   */
  protected fun <T> Observable<T>.untilCleared(): Observable<T> {
    return compose { upstream -> upstream.takeUntil(clearEvents) }
  }

  /**
   * Auto terminates the current [Flowable] when `onCleared` occurs.
   */
  protected fun <T> Flowable<T>.untilCleared(): Flowable<T> {
    return compose { upstream -> upstream.takeUntil(clearEvents.toFlowable(LATEST)) }
  }

  /**
   * Auto terminates the current [Single] when `onCleared` occurs.
   */
  protected fun <T> Single<T>.untilCleared(): Single<T> {
    return compose { upstream -> upstream.takeUntil(clearEvents.toFlowable(LATEST)) }
  }

  @CallSuper
  override fun onCleared() {
    clearEventsSubject.accept(0)
  }
}