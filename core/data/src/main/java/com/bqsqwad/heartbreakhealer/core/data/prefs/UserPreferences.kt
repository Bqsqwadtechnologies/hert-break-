package com.bqsqwad.heartbreakhealer.core.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

@Singleton
class UserPreferences @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private val ds = context.dataStore

    val onboardingComplete: Flow<Boolean> = ds.data.map { it[Keys.ONBOARDING] ?: false }
    val nickname: Flow<String> = ds.data.map { it[Keys.NICKNAME] ?: "" }
    val heartsBalance: Flow<Int> = ds.data.map { it[Keys.HEARTS] ?: 50 }
    val noContactDays: Flow<Int> = ds.data.map { it[Keys.NC_DAYS] ?: 0 }
    val noContactBest: Flow<Int> = ds.data.map { it[Keys.NC_BEST] ?: 0 }
    val noContactStartEpoch: Flow<Long?> = ds.data.map { it[Keys.NC_START_EPOCH] }
    val reduceMotion: Flow<Boolean> = ds.data.map { it[Keys.REDUCE_MOTION] ?: false }
    val dailyRemindersEnabled: Flow<Boolean> = ds.data.map { it[Keys.DAILY_REMINDERS] ?: false }
    val isFirstLaunch: Flow<Boolean> = ds.data.map { it[Keys.FIRST_LAUNCH] ?: true }
    val ageConfirmed: Flow<Boolean> = ds.data.map { it[Keys.AGE_CONFIRMED] ?: false }
    val urgePassedCount: Flow<Int> = ds.data.map { it[Keys.URGE_PASSED] ?: 0 }
    val savedMemories: Flow<Set<String>> = ds.data.map { it[Keys.MEMORIES] ?: emptySet() }
    val savedTriggers: Flow<Set<String>> = ds.data.map { it[Keys.TRIGGERS] ?: emptySet() }
    val completedPathDays: Flow<Set<String>> = ds.data.map { it[Keys.PATH_DAYS] ?: emptySet() }
    val completedRebuildDays: Flow<Set<String>> = ds.data.map { it[Keys.REBUILD_DAYS] ?: emptySet() }
    val lastJourneyCompletionDate: Flow<String?> = ds.data.map { it[Keys.LAST_JOURNEY_COMPLETION_DATE] }
    val timeCapsuleText: Flow<String?> = ds.data.map { it[Keys.CAPSULE_TEXT] }
    val timeCapsuleUnlock: Flow<String?> = ds.data.map { it[Keys.CAPSULE_UNLOCK] }
    val breakupDayEpoch: Flow<Long?> = ds.data.map { it[Keys.BREAKUP_EPOCH] }

    suspend fun setOnboardingComplete(nickname: String, goals: Set<String>) {
        ds.edit {
            it[Keys.ONBOARDING] = true
            it[Keys.NICKNAME] = nickname
            it[Keys.GOALS] = goals
            it[Keys.FIRST_LAUNCH] = false
        }
    }

    suspend fun setHearts(balance: Int) {
        ds.edit { it[Keys.HEARTS] = balance.coerceAtLeast(0) }
    }

    suspend fun spendHearts(amount: Int): Boolean {
        if (amount <= 0) return false
        var spent = false
        ds.edit {
            val current = it[Keys.HEARTS] ?: 50
            if (current >= amount) {
                it[Keys.HEARTS] = current - amount
                spent = true
            }
        }
        return spent
    }

    suspend fun addHearts(amount: Int) {
        ds.edit {
            val current = it[Keys.HEARTS] ?: 50
            it[Keys.HEARTS] = (current + amount).coerceAtLeast(0)
        }
    }

    suspend fun setNoContactStartEpoch(epochDay: Long) {
        ds.edit { it[Keys.NC_START_EPOCH] = epochDay }
    }

    suspend fun setNoContactDays(days: Int, best: Int) {
        ds.edit {
            it[Keys.NC_DAYS] = days.coerceAtLeast(0)
            it[Keys.NC_BEST] = best.coerceAtLeast(0)
        }
    }

    suspend fun setDailyRemindersEnabled(enabled: Boolean) {
        ds.edit { it[Keys.DAILY_REMINDERS] = enabled }
    }

    suspend fun setReduceMotion(enabled: Boolean) {
        ds.edit { it[Keys.REDUCE_MOTION] = enabled }
    }

    suspend fun setAgeConfirmed(confirmed: Boolean) {
        ds.edit { it[Keys.AGE_CONFIRMED] = confirmed }
    }

    suspend fun incrementUrgePassed() {
        ds.edit {
            val c = it[Keys.URGE_PASSED] ?: 0
            it[Keys.URGE_PASSED] = c + 1
        }
    }

    /**
     * Atomically records a journey task and a shared daily lock.
     * The 14-day and 30-day journeys share this lock so only one task across
     * both programs can be completed on a calendar day.
     */
    suspend fun tryCompleteJourneyDay(
        journey: String,
        day: Int,
        alreadyCompletedDays: Set<Int>,
        today: String
    ): Boolean {
        var completed = false
        ds.edit { data ->
            val completionKey = when (journey) {
                JOURNEY_REBUILD -> Keys.REBUILD_DAYS
                JOURNEY_HEALING_PATH -> Keys.PATH_DAYS
                else -> return@edit
            }
            val maximumDays = if (journey == JOURNEY_REBUILD) 30 else 14
            val storedDays = data[completionKey].orEmpty().mapNotNull(String::toIntOrNull).toSet()
            val allCompletedDays = storedDays + alreadyCompletedDays
            var nextDay = 1
            while (nextDay in allCompletedDays && nextDay <= maximumDays) nextDay++

            if (day !in 1..maximumDays ||
                day != nextDay ||
                data[Keys.LAST_JOURNEY_COMPLETION_DATE] == today
            ) {
                return@edit
            }

            data[completionKey] = storedDays + day.toString()
            data[Keys.LAST_JOURNEY_COMPLETION_DATE] = today
            completed = true
        }
        return completed
    }

    suspend fun completePathDay(day: Int) {
        ds.edit { data -> data[Keys.PATH_DAYS] = (data[Keys.PATH_DAYS] ?: emptySet()) + day.toString() }
    }

    suspend fun addMemory(title: String, note: String) {
        ds.edit { data ->
            val current = data[Keys.MEMORIES] ?: emptySet()
            data[Keys.MEMORIES] = current + "${title.replace("\\t", " ").replace("\\n", " ")}\\t${note.replace("\\t", " ").replace("\\n", " ")}"
        }
    }

    suspend fun addTrigger(name: String, coping: String) {
        ds.edit { data ->
            val current = data[Keys.TRIGGERS] ?: emptySet()
            data[Keys.TRIGGERS] = current + "${name.replace("\\t", " ").replace("\\n", " ")}\\t${coping.replace("\\t", " ").replace("\\n", " ")}"
        }
    }

    suspend fun setTimeCapsule(text: String, unlockDate: String) {
        ds.edit {
            it[Keys.CAPSULE_TEXT] = text
            it[Keys.CAPSULE_UNLOCK] = unlockDate
        }
    }

    suspend fun setBreakupEpoch(epochDay: Long) {
        ds.edit { it[Keys.BREAKUP_EPOCH] = epochDay }
    }

    suspend fun clearAll() {
        ds.edit { it.clear() }
    }

    companion object {
        const val JOURNEY_REBUILD = "rebuild"
        const val JOURNEY_HEALING_PATH = "healing_path"
    }

    private object Keys {
        val ONBOARDING = booleanPreferencesKey("onboarding_complete")
        val NICKNAME = stringPreferencesKey("nickname")
        val GOALS = stringSetPreferencesKey("goals")
        val HEARTS = intPreferencesKey("hearts_balance")
        val NC_DAYS = intPreferencesKey("nc_days")
        val NC_BEST = intPreferencesKey("nc_best")
        val NC_START_EPOCH = androidx.datastore.preferences.core.longPreferencesKey("nc_start_epoch")
        val REDUCE_MOTION = booleanPreferencesKey("reduce_motion")
        val DAILY_REMINDERS = booleanPreferencesKey("daily_reminders_enabled")
        val MEMORIES = stringSetPreferencesKey("saved_memories")
        val TRIGGERS = stringSetPreferencesKey("saved_triggers")
        val PATH_DAYS = stringSetPreferencesKey("completed_path_days")
        val REBUILD_DAYS = stringSetPreferencesKey("completed_rebuild_days")
        val LAST_JOURNEY_COMPLETION_DATE = stringPreferencesKey("last_journey_completion_date")
        val FIRST_LAUNCH = booleanPreferencesKey("first_launch")
        val AGE_CONFIRMED = booleanPreferencesKey("age_confirmed")
        val URGE_PASSED = intPreferencesKey("urge_passed")
        val CAPSULE_TEXT = stringPreferencesKey("capsule_text")
        val CAPSULE_UNLOCK = stringPreferencesKey("capsule_unlock")
        val BREAKUP_EPOCH = androidx.datastore.preferences.core.longPreferencesKey("breakup_epoch")
    }
}
