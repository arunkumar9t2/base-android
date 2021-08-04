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

package dev.arunkumar.android.dagger.workmanager

import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import dagger.BindsInstance
import dagger.MapKey
import javax.inject.Provider
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass

@Target(
  FUNCTION,
  PROPERTY_GETTER,
  PROPERTY_SETTER
)
@Retention(RUNTIME)
@MapKey
annotation class WorkerKey(val value: KClass<out ListenableWorker>)

/**
 * Base interface for a dagger component that exposes a binding for all workers in a map with key
 * as the class name and value as the provider.
 */
interface WorkerSubComponent {

  fun workers(): Map<Class<out ListenableWorker>, Provider<ListenableWorker>>

  interface Factory {
    fun create(@BindsInstance workerParameters: WorkerParameters): WorkerSubComponent
  }
}