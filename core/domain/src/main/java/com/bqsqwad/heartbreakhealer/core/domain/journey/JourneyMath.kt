package com.bqsqwad.heartbreakhealer.core.domain.journey

const val JOURNEY_DAYS = 30
const val REQUIRED_TASKS_PER_DAY = 3
const val JOKES_REQUIRED = 2

/** What the user has done today towards the three required tasks. */
data class DayProgress(val story: Boolean, val jokes: Int, val rebuildStep: Boolean) {
    val jokesDone: Boolean get() = jokes >= JOKES_REQUIRED
    val doneCount: Int get() = (if (story) 1 else 0) + (if (jokesDone) 1 else 0) + (if (rebuildStep) 1 else 0)
    val complete: Boolean get() = doneCount == REQUIRED_TASKS_PER_DAY
}

/** Everything the Home heart, the journey screen and the notifications need. */
data class JourneyState(
    val asked: Boolean = false,
    val joined: Boolean = false,
    val completedDays: Int = 0,
    val today: DayProgress = DayProgress(false, 0, false),
    val todayCounted: Boolean = false,
    val doneToday: Set<String> = emptySet(),
    val finished: Boolean = false,
    val celebrationPending: Boolean = false,
    val dayCountedPending: Boolean = false,
    val celebrateUntilEpochDay: Long = 0L,
    val roundsFinished: Int = 0,
    val reminderEnabled: Boolean = true,
    val reminderHour: Int = 19,
    val companionLastShownEpochDay: Long = 0L,
) {
    val percent: Int get() = JourneyMath.percent(completedDays, today, todayCounted)
    val daysToGo: Int get() = JourneyMath.daysToGo(completedDays)
    /** 0f = fully broken, 1f = fully healed. */
    val heartProgress: Float get() = JourneyMath.heartProgress(completedDays, today, todayCounted)
    fun celebrating(todayEpochDay: Long): Boolean = finished && todayEpochDay <= celebrateUntilEpochDay
}

object JourneyMath {
    fun heartProgress(completedDays: Int, today: DayProgress, todayCounted: Boolean): Float {
        val done = completedDays.coerceIn(0, JOURNEY_DAYS)
        if (done >= JOURNEY_DAYS) return 1f
        val fraction = if (todayCounted) 0f else today.doneCount / REQUIRED_TASKS_PER_DAY.toFloat()
        return ((done + fraction) / JOURNEY_DAYS).coerceIn(0f, 1f)
    }

    /** Whole percent. It only reaches 100 when day 30 has been counted. */
    fun percent(completedDays: Int, today: DayProgress, todayCounted: Boolean): Int {
        if (completedDays >= JOURNEY_DAYS) return 100
        return (heartProgress(completedDays, today, todayCounted) * 100f).toInt().coerceIn(0, 99)
    }

    fun daysToGo(completedDays: Int): Int = (JOURNEY_DAYS - completedDays).coerceIn(0, JOURNEY_DAYS)

    /**
     * Is today's third task satisfied? Normally this is the existing shared lock.
     * If the user already finished BOTH older programs the lock can never be set again,
     * so any one optional task done today counts instead.
     */
    fun rebuildStepDone(lockDate: String?, today: String, allOlderProgramsDone: Boolean, optionalDoneToday: Boolean): Boolean =
        lockDate == today || (allOlderProgramsDone && optionalDoneToday)
}
