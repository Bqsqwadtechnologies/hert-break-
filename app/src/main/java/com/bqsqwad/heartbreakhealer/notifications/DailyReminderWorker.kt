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
        val period = inputData.getString(KEY_PERIOD) ?: PERIOD_MORNING
        val (title, message, targetExtra) = when (period) {
            PERIOD_AFTERNOON -> Triple(
                "Afternoon Breath & Reset",
                ReminderMessageBank.afternoon(),
                EXTRA_OPEN_BREATHWORK
            )
            PERIOD_EVENING -> Triple(
                "Evening Wind-Down & Soundscapes",
                ReminderMessageBank.evening(),
                EXTRA_OPEN_SOUNDSCAPES
            )
            else -> Triple(
                "A gentle note for today",
                ReminderMessageBank.today(),
                EXTRA_OPEN_STORIES
            )
        }

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(targetExtra, true)
        }
        val notifId = when (period) {
            PERIOD_AFTERNOON -> 1002
            PERIOD_EVENING -> 1003
            else -> 1001
        }
        val pending = PendingIntent.getActivity(
            applicationContext,
            notifId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(applicationContext, HeartbreakHealerApp.CHANNEL_DAILY)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .setBigContentTitle(title)
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
            manager.notify(notifId, notification)
            Result.success()
        } catch (_: SecurityException) {
            Result.success()
        }
    }

    companion object {
        const val KEY_PERIOD = "notification_period"
        const val PERIOD_MORNING = "morning"
        const val PERIOD_AFTERNOON = "afternoon"
        const val PERIOD_EVENING = "evening"

        const val EXTRA_OPEN_STORIES = "open_stories_from_reminder"
        const val EXTRA_OPEN_SOUNDSCAPES = "open_soundscapes_from_reminder"
        const val EXTRA_OPEN_BREATHWORK = "open_breathwork_from_reminder"
    }
}
