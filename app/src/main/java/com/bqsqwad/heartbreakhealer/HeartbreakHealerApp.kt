package com.bqsqwad.heartbreakhealer

import com.bqsqwad.heartbreakhealer.core.data.journey.HeartJourneyRepository
import com.bqsqwad.heartbreakhealer.core.data.prefs.UserPreferences
import com.bqsqwad.heartbreakhealer.notifications.NotificationScheduler
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HeartbreakHealerApp : Application() {
    @Inject lateinit var journey: HeartJourneyRepository
    @Inject lateinit var preferences: UserPreferences
    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        CrashReportStore.install(this)
        createNotificationChannels()
        journey.start(appScope) // counts a journey day the moment all three tasks are done
        appScope.launch {
            if (preferences.reactivateDailyRemindersOnce()) {
                NotificationScheduler.scheduleDaily(this@HeartbreakHealerApp)
            }
        }
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(NotificationManager::class.java)
            val dailyChannel = NotificationChannel(
                CHANNEL_DAILY,
                getString(R.string.channel_daily_name),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply { description = getString(R.string.channel_daily_desc) }
            val tipsChannel = NotificationChannel(
                CHANNEL_TIPS,
                getString(R.string.channel_tips_name),
                NotificationManager.IMPORTANCE_LOW
            ).apply { description = getString(R.string.channel_tips_desc) }
            manager.createNotificationChannels(listOf(dailyChannel, tipsChannel))
        }
    }

    companion object {
        const val CHANNEL_DAILY = "daily_reminders"
        const val CHANNEL_TIPS = "tips"
    }
}
