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

package dev.arunkumar.android.util.work

import dagger.Subcomponent
import dev.arunkumar.android.dagger.workmanager.WorkerSubComponent
import dev.arunkumar.android.tasks.actions.DeleteTaskWorker

@Subcomponent(
  modules = [
    DeleteTaskWorker.Builder::class
  ]
)
interface SampleWorkerSubComponent : WorkerSubComponent {
  @Subcomponent.Factory
  interface Factory : WorkerSubComponent.Factory
}
