package com.bqsqwad.heartbreakhealer.notifications

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import java.time.Duration
import java.time.ZonedDateTime

object NotificationScheduler {
    private const val WORK_NAME = "daily_reminder"

    fun scheduleDaily(context: Context, hour: Int = 9) {
        val now = ZonedDateTime.now()
        val nextRun = now.withHour(hour.coerceIn(0, 23)).withMinute(0).withSecond(0).withNano(0)
            .let { if (it.isAfter(now)) it else it.plusDays(1) }
        val initialDelay = Duration.between(now, nextRun).toMillis()
        val request = PeriodicWorkRequestBuilder<DailyReminderWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    fun cancel(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
    }
}
