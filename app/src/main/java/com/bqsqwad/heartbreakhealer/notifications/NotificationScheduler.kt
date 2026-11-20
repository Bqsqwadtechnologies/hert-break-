package com.bqsqwad.heartbreakhealer.notifications

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import java.time.Duration
import java.time.ZonedDateTime

object NotificationScheduler {
    private const val WORK_LEGACY = "daily_reminder"
    private const val WORK_MORNING = "daily_reminder_morning"
    private const val WORK_AFTERNOON = "daily_reminder_afternoon"
    private const val WORK_EVENING = "daily_reminder_evening"

    fun scheduleDaily(context: Context, hour: Int = 9) {
        // Morning reflection and daily check-in (default 9:00 AM)
        schedulePeriodic(context, WORK_MORNING, hour.coerceIn(0, 23), 0, DailyReminderWorker.PERIOD_MORNING)
        // Midday nervous system reset and somatic breathwork (2:00 PM / 14:00)
        schedulePeriodic(context, WORK_AFTERNOON, 14, 0, DailyReminderWorker.PERIOD_AFTERNOON)
        // Evening wind-down and calming soundscapes (8:30 PM / 20:30)
        schedulePeriodic(context, WORK_EVENING, 20, 30, DailyReminderWorker.PERIOD_EVENING)
    }

    private fun schedulePeriodic(context: Context, workName: String, hour: Int, minute: Int, period: String) {
        val now = ZonedDateTime.now()
        val nextRun = now.withHour(hour).withMinute(minute).withSecond(0).withNano(0)
            .let { if (it.isAfter(now)) it else it.plusDays(1) }
        val initialDelay = Duration.between(now, nextRun).toMillis()
        val data = Data.Builder()
            .putString(DailyReminderWorker.KEY_PERIOD, period)
            .build()
        val request = PeriodicWorkRequestBuilder<DailyReminderWorker>(24, TimeUnit.HOURS)
            .setInputData(data)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            workName,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    fun cancel(context: Context) {
        val wm = WorkManager.getInstance(context)
        wm.cancelUniqueWork(WORK_LEGACY)
        wm.cancelUniqueWork(WORK_MORNING)
        wm.cancelUniqueWork(WORK_AFTERNOON)
        wm.cancelUniqueWork(WORK_EVENING)
    }
}
