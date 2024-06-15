package com.xhhold.flutter_record

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService

class RecordNotificationManager(private val context: RecordService) {
    private val notificationManager by lazy { context.getSystemService<NotificationManager>() }

    companion object {
        const val TAG = "RecordNotificationManager"
        const val NOTIFICATION_ID = 0x01
        const val CHANNEL_ID = "com.xhhold.flutter_record.channel"
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager?.getNotificationChannel(CHANNEL_ID) == null) {
                val channel = NotificationChannel(
                    CHANNEL_ID, "录屏", NotificationManager.IMPORTANCE_MIN
                )
                notificationManager?.createNotificationChannel(channel)
            }
        }
    }

    fun start() {
        val notification = buildNotification("准备就绪")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                context.startForeground(
                    NOTIFICATION_ID,
                    notification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION or ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE or ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
                )
            } else {
                context.startForeground(
                    NOTIFICATION_ID,
                    notification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION or ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
                )
            }
        } else {
            context.startForeground(NOTIFICATION_ID, notification)
        }
    }

    fun update(content: String) {
        notificationManager?.notify(NOTIFICATION_ID, buildNotification(content))
    }

    fun stop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.stopForeground(Service.STOP_FOREGROUND_REMOVE)
        } else {
            context.stopForeground(true)
        }
    }

    private fun buildNotification(content: String) =
        NotificationCompat.Builder(context, CHANNEL_ID).setSmallIcon(R.drawable.ic_record)
            .setContentTitle("录制").setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH).setAutoCancel(false)
            .setCategory(NotificationCompat.CATEGORY_SERVICE).build()
}