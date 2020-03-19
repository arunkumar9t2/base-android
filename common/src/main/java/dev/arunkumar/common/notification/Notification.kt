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