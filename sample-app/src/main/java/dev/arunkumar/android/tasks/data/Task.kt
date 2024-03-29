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

package dev.arunkumar.android.tasks.data

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*

open class Task(
  @PrimaryKey
  @Required
  var id: UUID = UUID.randomUUID(),
  var name: String = "",
  var description: String = "",
  var tags: RealmList<Tag> = RealmList(),
  var completed: Boolean = false,
  var createdAt: Date = Date(),
  var estimate: Long = 0L,
  var progress: Int = 0
) : RealmObject() {
  init {
    require(progress in 0..100) {
      "Progress should be between 0 to 100"
    }
  }
}

open class Tag(
  @PrimaryKey
  var tagName: String = ""
) : RealmObject()
