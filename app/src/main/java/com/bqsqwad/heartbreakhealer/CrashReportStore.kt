package com.bqsqwad.heartbreakhealer

import android.content.Context
import android.util.Log
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object CrashReportStore {
    private const val PREFS = "heartbreak_healer_crash_reports"
    private const val KEY_REPORT = "last_crash_report"
    private const val KEY_TIME = "last_crash_time"
    private const val TAG = "HeartbreakHealerCrash"

    fun install(context: Context) {
        val app = context.applicationContext
        val previous = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, error ->
            try {
                val writer = StringWriter()
                error.printStackTrace(PrintWriter(writer))
                val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US).format(Date())
                app.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
                    .putString(KEY_REPORT, "Heartbreak Healer crash report\nTime: $timestamp\nThread: ${thread.name}\nAndroid: ${android.os.Build.VERSION.RELEASE} (API ${android.os.Build.VERSION.SDK_INT})\nDevice: ${android.os.Build.MANUFACTURER} ${android.os.Build.MODEL}\n\n${writer}")
                    .putString(KEY_TIME, timestamp)
                    .commit()
                Log.e(TAG, "Saved fatal crash report", error)
            } catch (storeError: Throwable) {
                Log.e(TAG, "Could not persist crash report", storeError)
            }
            previous?.uncaughtException(thread, error)
        }
    }

    fun read(context: Context): String? =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY_REPORT, null)

    fun clear(context: Context) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
            .remove(KEY_REPORT).remove(KEY_TIME).apply()
    }
}
