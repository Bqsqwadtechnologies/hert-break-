package com.bqsqwad.heartbreakhealer.feature.heartjourney

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.bqsqwad.heartbreakhealer.core.data.journey.HeartJourneyRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import java.time.Duration
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

object JourneyNotifications {
    const val CHANNEL = "heart_journey"
    const val EXTRA_OPEN_JOURNEY = "open_heart_journey"
    const val ID_DAILY = 2001
    const val ID_CELEBRATE = 2101

    fun ensureChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= 26) {
            context.getSystemService(NotificationManager::class.java).createNotificationChannel(
                NotificationChannel(CHANNEL, "Heart rebuild", NotificationManager.IMPORTANCE_DEFAULT)
                    .apply { description = "Daily encouragement and celebration for your 30-day heart rebuild" },
            )
        }
    }

    fun show(context: Context, id: Int, title: String, body: String) {
        ensureChannel(context)
        val launch = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(EXTRA_OPEN_JOURNEY, true)
        }
        val pending = launch?.let { PendingIntent.getActivity(context, id, it, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE) }
        val icon = context.resources.getIdentifier("ic_notification", "drawable", context.packageName).takeIf { it != 0 } ?: android.R.drawable.ic_dialog_info
        val n = NotificationCompat.Builder(context, CHANNEL)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setContentIntent(pending)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .build()
        try { context.getSystemService(NotificationManager::class.java).notify(id, n) } catch (_: SecurityException) { /* permission revoked */ }
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface JourneyEntryPoint { fun repo(): HeartJourneyRepository }

class JourneyReminderWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val repo = EntryPointAccessors.fromApplication(applicationContext, JourneyEntryPoint::class.java).repo()
        val s = repo.snapshot()
        if (!s.joined || !s.reminderEnabled) return Result.success()
        val today = LocalDate.now().toEpochDay()
        if (s.finished) return Result.success() // the celebration notifications take over
        val body = if (s.todayCounted) JourneyMessages.doneTodayFor(today)
        else JourneyMessages.reminderFor(today, s.today.doneCount, s.daysToGo, s.percent)
        val title = if (s.todayCounted) "Today is complete" else JourneyMessages.dailyFor(s.completedDays).substringBefore('.')
        JourneyNotifications.show(applicationContext, JourneyNotifications.ID_DAILY, title, body)
        return Result.success()
    }
}

class JourneyCelebrationWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val stage = inputData.getInt("stage", 0).coerceIn(0, 2)
        val title = when (stage) { 0 -> "Your heart is healed 🌸"; 1 -> "Still celebrating you"; else -> "Keep checking in" }
        JourneyNotifications.show(applicationContext, JourneyNotifications.ID_CELEBRATE + stage, title, JourneyMessages.celebration[stage])
        return Result.success()
    }
}

object JourneyScheduler {
    private const val DAILY = "heart_journey_daily"

    fun scheduleDaily(context: Context, hour: Int) {
        val now = ZonedDateTime.now()
        val next = now.withHour(hour.coerceIn(0, 23)).withMinute(0).withSecond(0).withNano(0).let { if (it.isAfter(now)) it else it.plusDays(1) }
        val request = PeriodicWorkRequestBuilder<JourneyReminderWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(Duration.between(now, next).toMillis(), TimeUnit.MILLISECONDS).build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(DAILY, ExistingPeriodicWorkPolicy.UPDATE, request)
    }

    fun cancelDaily(context: Context) { WorkManager.getInstance(context).cancelUniqueWork(DAILY) }

    /** Immediately, then 24 hours and 48 hours later: two days of celebration, then "keep checking in". */
    fun celebrate(context: Context) {
        val wm = WorkManager.getInstance(context)
        listOf(0 to 0L, 1 to 24L, 2 to 48L).forEach { (stage, hours) ->
            val request = OneTimeWorkRequestBuilder<JourneyCelebrationWorker>()
                .setInputData(Data.Builder().putInt("stage", stage).build())
                .setInitialDelay(hours, TimeUnit.HOURS).build()
            wm.enqueueUniqueWork("heart_journey_celebrate_$stage", ExistingWorkPolicy.KEEP, request)
        }
    }
}
