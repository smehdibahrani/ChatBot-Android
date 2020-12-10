package ir.chatbot.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.PowerManager

import androidx.core.app.NotificationCompat

import ir.chatbot.R


object NotificationUtil {
    private const val NOTIFICATION_ID = 234
    private const val CHANNEL_ID = "my_channel_01"


    fun showNotification(context: Context, title: String, body: String, intent: Intent) {

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT)


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val name = "my_channel"
            val desc = "This is my channel"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            mChannel.description = desc
            mChannel.setShowBadge(true)
            mChannel.enableLights(true)
            mChannel.lightColor = Color.RED
            mChannel.enableVibration(true)
           // mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)

            notificationManager.createNotificationChannel(mChannel)
        }
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(R.drawable.app)
            .setContentTitle(title)
            //.setSubText(notification.Summary)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setContentText(body)
            //.setContentInfo(notification.Summary)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)


        notificationBuilder.setContentIntent(pendingIntent)


        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())

        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        @SuppressLint("InvalidWakeLockTag")
        val wakeLock = pm.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK or
                    PowerManager.ACQUIRE_CAUSES_WAKEUP or
                    PowerManager.ON_AFTER_RELEASE, "WakeLock"
        )

        wakeLock.acquire(5000)
    }


}




