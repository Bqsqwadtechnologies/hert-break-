package com.bqsqwad.heartbreakhealer.core.data.journey

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.bqsqwad.heartbreakhealer.core.data.prefs.UserPreferences
import com.bqsqwad.heartbreakhealer.core.domain.journey.DayProgress
import com.bqsqwad.heartbreakhealer.core.domain.journey.JOURNEY_DAYS
import com.bqsqwad.heartbreakhealer.core.domain.journey.JourneyMath
import com.bqsqwad.heartbreakhealer.core.domain.journey.JourneyState
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private val Context.journeyStore: DataStore<Preferences> by preferencesDataStore(name = "heart_journey")

/** Names of auto-detected checklist items. */
object JourneyEvents {
    const val JOURNAL = "journal"
    const val CHECKIN = "checkin"
    const val SELFCARE = "selfcare"
    const val LETTER = "letter"
    const val URGE = "urge"
    const val EVENING = "evening"
    const val MEMORY = "memory"
    const val TRIGGER = "trigger"
    const val CAPSULE = "capsule"
    fun visit(route: String) = "visit:$route"

    /** Events that count as "one optional task" when the older programs are all finished. */
    val TASK_EVENTS = setOf(JOURNAL, CHECKIN, SELFCARE, LETTER, EVENING, MEMORY, TRIGGER, CAPSULE)
}

@Singleton
class HeartJourneyRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val prefs: UserPreferences,
) {
    private val ds get() = context.journeyStore

    private object K {
        val ASKED = booleanPreferencesKey("asked")
        val JOINED = booleanPreferencesKey("joined")
        val DONE_DAYS = intPreferencesKey("done_days")
        val LAST_COUNTED = stringPreferencesKey("last_counted")
        val TODAY = stringPreferencesKey("today")
        val T_STORY = booleanPreferencesKey("t_story")
        val T_JOKES = stringSetPreferencesKey("t_jokes")
        val T_EVENTS = stringSetPreferencesKey("t_events")
        val FINISHED = booleanPreferencesKey("finished")
        val CELEBRATION_PENDING = booleanPreferencesKey("celebration_pending")
        val DAY_PENDING = booleanPreferencesKey("day_pending")
        val CELEBRATE_UNTIL = longPreferencesKey("celebrate_until")
        val ROUNDS = intPreferencesKey("rounds")
        val REMINDER_ON = booleanPreferencesKey("reminder_on")
        val REMINDER_HOUR = intPreferencesKey("reminder_hour")
        val COMPANION_SHOWN = longPreferencesKey("companion_shown")
    }

    private fun MutablePreferences.rollover(today: String) {
        if (this[K.TODAY] != today) {
            this[K.TODAY] = today
            remove(K.T_STORY); remove(K.T_JOKES); remove(K.T_EVENTS)
        }
    }

    /** Ticks every minute so the screen rolls over at midnight without a restart. */
    private val clock: Flow<String> = flow { while (true) { emit(LocalDate.now().toString()); delay(60_000) } }

    val state: Flow<JourneyState> = combine(ds.data, clock, prefs.lastJourneyCompletionDate, prefs.completedRebuildDays, prefs.completedPathDays) {
            p, today, lock, rebuildDays, pathDays ->
        val fresh = p[K.TODAY] == today
        val events = if (fresh) p[K.T_EVENTS].orEmpty() else emptySet()
        val older = rebuildDays.size >= JOURNEY_DAYS && pathDays.size >= 14
        val optionalDone = events.any { it in JourneyEvents.TASK_EVENTS }
        val progress = DayProgress(
            story = fresh && p[K.T_STORY] == true,
            jokes = if (fresh) p[K.T_JOKES].orEmpty().size else 0,
            rebuildStep = JourneyMath.rebuildStepDone(lock, today, older, optionalDone),
        )
        JourneyState(
            asked = p[K.ASKED] == true,
            joined = p[K.JOINED] == true,
            completedDays = (p[K.DONE_DAYS] ?: 0).coerceIn(0, JOURNEY_DAYS),
            today = progress,
            todayCounted = p[K.LAST_COUNTED] == today,
            doneToday = events,
            finished = p[K.FINISHED] == true,
            celebrationPending = p[K.CELEBRATION_PENDING] == true,
            dayCountedPending = p[K.DAY_PENDING] == true,
            celebrateUntilEpochDay = p[K.CELEBRATE_UNTIL] ?: 0L,
            roundsFinished = p[K.ROUNDS] ?: 0,
            reminderEnabled = p[K.REMINDER_ON] != false,
            reminderHour = p[K.REMINDER_HOUR] ?: 19,
            companionLastShownEpochDay = p[K.COMPANION_SHOWN] ?: 0L,
        )
    }

    /** Called once from Application.onCreate. Counts a day the moment all three tasks are done, even if the UI is closed. */
    fun start(scope: CoroutineScope) {
        scope.launch { state.collect { s -> evaluate(s) } }
    }

    private suspend fun evaluate(s: JourneyState) {
        if (!s.joined || s.finished || s.todayCounted || !s.today.complete) return
        val today = LocalDate.now()
        ds.edit { p ->
            p.rollover(today.toString())
            if (p[K.JOINED] != true || p[K.FINISHED] == true || p[K.LAST_COUNTED] == today.toString()) return@edit
            val done = (p[K.DONE_DAYS] ?: 0) + 1
            p[K.DONE_DAYS] = done
            p[K.LAST_COUNTED] = today.toString()
            if (done >= JOURNEY_DAYS) {
                p[K.FINISHED] = true
                p[K.CELEBRATION_PENDING] = true
                p[K.CELEBRATE_UNTIL] = today.toEpochDay() + 2
                p[K.ROUNDS] = (p[K.ROUNDS] ?: 0) + 1
            } else {
                p[K.DAY_PENDING] = true
            }
        }
    }

    // ----- one-time question and joining -----
    suspend fun answerBroken(yes: Boolean) {
        ds.edit { p ->
            p[K.ASKED] = true
            if (yes) resetRound(p)
        }
    }

    /** Used by "Start my 30-day heart rebuild" and by "Broken again". */
    suspend fun startRound() { ds.edit { p -> p[K.ASKED] = true; resetRound(p) } }

    private fun resetRound(p: MutablePreferences) {
        p[K.JOINED] = true
        p[K.DONE_DAYS] = 0
        p[K.FINISHED] = false
        p[K.CELEBRATION_PENDING] = false
        p[K.DAY_PENDING] = false
        p.remove(K.LAST_COUNTED)
        p.rollover(LocalDate.now().toString())
    }

    // ----- events from the existing features (one line each in DashboardViewModel) -----
    suspend fun recordStory() {
        val today = LocalDate.now().toString()
        ds.edit { p -> p.rollover(today); p[K.T_STORY] = true }
    }

    suspend fun recordJoke(jokeId: String) {
        if (jokeId.isBlank()) return
        val today = LocalDate.now().toString()
        ds.edit { p -> p.rollover(today); p[K.T_JOKES] = p[K.T_JOKES].orEmpty() + jokeId }
    }

    suspend fun recordEvent(event: String) {
        val today = LocalDate.now().toString()
        ds.edit { p -> p.rollover(today); p[K.T_EVENTS] = p[K.T_EVENTS].orEmpty() + event }
    }

    // ----- UI acknowledgements -----
    suspend fun ackDayCounted() { ds.edit { it[K.DAY_PENDING] = false } }
    suspend fun ackCelebration() { ds.edit { it[K.CELEBRATION_PENDING] = false } }
    suspend fun setReminder(enabled: Boolean, hour: Int) { ds.edit { it[K.REMINDER_ON] = enabled; it[K.REMINDER_HOUR] = hour.coerceIn(0, 23) } }
    suspend fun markCompanionShown() { ds.edit { it[K.COMPANION_SHOWN] = LocalDate.now().toEpochDay() } }
    suspend fun clearAll() { ds.edit { it.clear() } }

    /** For workers (notifications) that run when the app UI is closed. */
    suspend fun snapshot(): JourneyState = state.first()
}
