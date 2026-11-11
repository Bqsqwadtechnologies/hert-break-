package com.bqsqwad.heartbreakhealer.core.data.repo

import com.bqsqwad.heartbreakhealer.core.data.prefs.UserPreferences
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HeartsRepository @Inject constructor(
    private val prefs: UserPreferences
) {
    val balance: Flow<Int> = prefs.heartsBalance

    suspend fun earnFromCheckIn() = prefs.addHearts(5)
    suspend fun earnFromSelfCare() = prefs.addHearts(3)
    suspend fun earnFromJournal() = prefs.addHearts(5)
    suspend fun earnFromNoContactMilestone() = prefs.addHearts(10)
    suspend fun earnFromRewardedAd() = prefs.addHearts(15)

    suspend fun spend(amount: Int): Boolean {
        var ok = false
        prefs.heartsBalance.collect { current ->
            if (current >= amount) {
                prefs.setHearts(current - amount)
                ok = true
            }
            return@collect
        }
        return ok
    }

    suspend fun setBalance(value: Int) = prefs.setHearts(value)
}
