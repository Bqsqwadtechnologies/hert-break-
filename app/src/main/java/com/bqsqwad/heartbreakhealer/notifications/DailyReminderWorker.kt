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
import com.bqsqwad.heartbreakhealer.feature.affirmations.AffirmationBank

class DailyReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val affirmation = AffirmationBank.today()
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pending = PendingIntent.getActivity(
            applicationContext, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(applicationContext, HeartbreakHealerApp.CHANNEL_DAILY)
            .setSmallIcon(R.drawable.ic_splash)
            .setContentTitle("Heartbreak Healer")
            .setContentText(affirmation)
            .setStyle(NotificationCompat.BigTextStyle().bigText(affirmation))
            .setContentIntent(pending)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(1001, notification)
        return Result.success()
    }
}
