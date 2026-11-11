package com.bqsqwad.heartbreakhealer.core.data.repo

import com.bqsqwad.heartbreakhealer.core.data.db.NoContactDao
import com.bqsqwad.heartbreakhealer.core.data.db.NoContactEventEntity
import com.bqsqwad.heartbreakhealer.core.data.prefs.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoContactRepository @Inject constructor(
    private val prefs: UserPreferences,
    private val dao: NoContactDao
) {
    val currentDays: Flow<Int> = prefs.noContactDays
    val bestDays: Flow<Int> = prefs.noContactBest

    suspend fun recordSlip() {
        val best = prefs.noContactBest.first()
        val current = prefs.noContactDays.first()
        val newBest = maxOf(best, current)
        prefs.setNoContactDays(0, newBest)
        dao.insert(
            NoContactEventEntity(
                type = "SLIP",
                date = LocalDate.now().toString(),
                note = "Gentle restart"
            )
        )
    }

    suspend fun incrementDay() {
        val current = prefs.noContactDays.first() + 1
        val best = maxOf(prefs.noContactBest.first(), current)
        prefs.setNoContactDays(current, best)
    }
}
