package com.bqsqwad.heartbreakhealer.core.data.repo

import com.bqsqwad.heartbreakhealer.core.data.db.NoContactDao
import com.bqsqwad.heartbreakhealer.core.data.db.NoContactEventEntity
import com.bqsqwad.heartbreakhealer.core.data.prefs.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoContactRepository @Inject constructor(
    private val prefs: UserPreferences,
    private val dao: NoContactDao
) {
    val currentDays: Flow<Int> = prefs.noContactStartEpoch.map { start ->
        start?.let { (LocalDate.now().toEpochDay() - it).coerceAtLeast(0).toInt() } ?: 0
    }
    val bestDays: Flow<Int> = kotlinx.coroutines.flow.combine(currentDays, prefs.noContactBest) { current, best -> maxOf(current, best) }

    suspend fun recordSlip() {
        val best = prefs.noContactBest.first()
        val current = currentDays.first()
        val newBest = maxOf(best, current)
        prefs.setNoContactDays(0, newBest)
        prefs.setNoContactStartEpoch(LocalDate.now().toEpochDay())
        dao.insert(
            NoContactEventEntity(
                type = "SLIP",
                date = LocalDate.now().toString(),
                note = "Gentle restart"
            )
        )
    }


}
