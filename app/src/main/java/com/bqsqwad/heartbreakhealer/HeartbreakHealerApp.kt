package com.bqsqwad.heartbreakhealer

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HeartbreakHealerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
        // Initialize ads in background – never blocks splash or first frame
        MobileAds.initialize(this) { }
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
