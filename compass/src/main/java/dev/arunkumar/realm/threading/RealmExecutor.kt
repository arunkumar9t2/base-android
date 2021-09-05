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

package dev.arunkumar.realm.threading

import android.os.Handler
import android.os.HandlerThread
import android.os.Process.THREAD_PRIORITY_BACKGROUND
import dev.arunkumar.common.os.HandlerExecutor
import java.util.concurrent.Executor

interface StoppableExecutor : Executor {
  fun stop()
}

class RealmExecutor(private val tag: String? = null) : StoppableExecutor {

  private val handlerThread by lazy {
    HandlerThread(
      tag ?: this::class.java.simpleName + hashCode(),
      THREAD_PRIORITY_BACKGROUND
    ).apply { start() }
  }

  private val handlerExecutor by lazy { HandlerExecutor(Handler(handlerThread.looper)) }

  override fun execute(command: Runnable) = handlerExecutor.execute(command)

  override fun stop() {
    handlerThread.quitSafely()
  }
}
