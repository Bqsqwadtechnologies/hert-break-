package com.bqsqwad.heartbreakhealer.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bqsqwad.heartbreakhealer.HeartbreakHealerApp
import com.bqsqwad.heartbreakhealer.MainActivity
import com.bqsqwad.heartbreakhealer.R

class DailyReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val message = ReminderMessageBank.today()
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(EXTRA_OPEN_STORIES, true)
        }
        val pending = PendingIntent.getActivity(
            applicationContext,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(applicationContext, HeartbreakHealerApp.CHANNEL_DAILY)
            // Android status-bar small icons must be monochrome; this resource is
            // generated from the official BQ/Heartbreak Healer brand mark.
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("A gentle note for today")
            .setContentText("Tap to read today's full message.")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .setBigContentTitle("A gentle note for today")
                    .bigText(message)
            )
            .setContentIntent(pending)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return try {
            manager.notify(NOTIFICATION_ID, notification)
            Result.success()
        } catch (_: SecurityException) {
            // Notification permission may have been denied or revoked.
            Result.success()
        }
    }

    companion object {
        const val EXTRA_OPEN_STORIES = "open_stories_from_reminder"
        private const val NOTIFICATION_ID = 1001
        private const val REQUEST_CODE = 1001
    }
}
