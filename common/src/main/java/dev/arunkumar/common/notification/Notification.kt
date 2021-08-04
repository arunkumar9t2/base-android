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

package dev.arunkumar.common.notification

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat

inline fun notification(
  context: Context,
  channelId: String,
  builder: NotificationCompat.Builder.() -> Unit
): Notification {
  val notificationBuilder = NotificationCompat.Builder(context, channelId)
  builder(notificationBuilder)
  return notificationBuilder.build()
}

class NotificationAction(
  @DrawableRes
  var icon: Int = 0,
  var title: CharSequence? = null,
  var pendingIntent: PendingIntent? = null
)

inline fun notificationAction(builder: NotificationAction.() -> Unit): NotificationCompat.Action {
  val notificationActionBuilder = NotificationAction()
  builder(notificationActionBuilder)
  return NotificationCompat.Action(
    notificationActionBuilder.icon,
    notificationActionBuilder.title,
    notificationActionBuilder.pendingIntent
  )
}

inline fun NotificationCompat.Builder.action(builder: NotificationAction.() -> Unit) {
  val notificationActionBuilder = NotificationAction()
  builder(notificationActionBuilder)
  addAction(
    notificationActionBuilder.icon,
    notificationActionBuilder.title,
    notificationActionBuilder.pendingIntent
  )
}