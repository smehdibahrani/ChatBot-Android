package ir.chatbot.utils

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import ir.chatbot.R
import java.text.SimpleDateFormat
import java.util.*

object Notify {
   const val NotificationChannelID = "1234564"

    fun showNotification(context: Context, message: String,intent: Intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val df = SimpleDateFormat.getDateTimeInstance()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mNotification = Notification.Builder(context, NotificationChannelID)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.app)
                .setContentTitle("chat bot: ${df.format(Date())}")
                .setStyle(
                    Notification.BigTextStyle()
                        .bigText(message)
                )
                .setContentText(message).build()

            mNotification.contentIntent = pendingIntent
            notificationManager.notify(Date().time.toInt(), mNotification)
        } else {
            val mNotification = Notification.Builder(context)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.app)
                .setContentTitle("Noti: ${df.format(Date())}")
                .setStyle(
                    Notification.BigTextStyle()
                        .bigText(message)
                )
                .setContentText(message).build()

            mNotification.contentIntent = pendingIntent
            notificationManager.notify(Date().time.toInt(), mNotification)

        }
    }

    fun cancelAll(context: Context){
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }
}

val Date.formattedDateString: String
    get() {
        val df = SimpleDateFormat.getDateTimeInstance()
        return df.format(this)
    }

val Long.toDate: Date
    get() = Date(this)