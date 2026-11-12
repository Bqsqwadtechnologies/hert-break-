package com.bqsqwad.heartbreakhealer.feature.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bqsqwad.heartbreakhealer.core.data.repo.HeartsRepository
import com.bqsqwad.heartbreakhealer.core.data.repo.JournalRepository
import com.bqsqwad.heartbreakhealer.core.data.repo.NoContactRepository
import com.bqsqwad.heartbreakhealer.core.data.prefs.UserPreferences
import com.bqsqwad.heartbreakhealer.core.data.db.AppDatabase
import com.bqsqwad.heartbreakhealer.core.data.db.SelfCareCompletionEntity
import com.bqsqwad.heartbreakhealer.core.data.db.RebuildProgressEntity
import java.time.LocalDate
import java.time.LocalDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val heartsRepository: HeartsRepository,
    private val journalRepository: JournalRepository,
    private val noContactRepository: NoContactRepository,
    private val preferences: UserPreferences,
    private val database: AppDatabase
) : ViewModel() {

    val hearts: StateFlow<Int> = heartsRepository.balance
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 50)

    val noContactDays: StateFlow<Int> = noContactRepository.currentDays
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val bestNoContactDays: StateFlow<Int> = noContactRepository.bestDays
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val journalEntries = journalRepository.getEntries()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val selfCareCompleted = database.selfCareDao().getCompletionsForDate(LocalDate.now().toString())
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val rebuildProgress = database.rebuildDao().getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val reduceMotion = preferences.reduceMotion
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    val dailyRemindersEnabled = preferences.dailyRemindersEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val capsuleText: StateFlow<String?> = preferences.timeCapsuleText
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    val capsuleUnlock: StateFlow<String?> = preferences.timeCapsuleUnlock
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun saveCapsule(text: String, days: Int) {
        viewModelScope.launch {
            preferences.setTimeCapsule(text, LocalDate.now().plusDays(days.toLong()).toString())
        }
    }

    fun setReduceMotion(enabled: Boolean) { viewModelScope.launch { preferences.setReduceMotion(enabled) } }
    fun setDailyRemindersEnabled(enabled: Boolean) { viewModelScope.launch { preferences.setDailyRemindersEnabled(enabled) } }

    fun completeSelfCare(taskId: String) {
        viewModelScope.launch {
            val date = LocalDate.now().toString()
            val key = "$date:$taskId"
            if (!database.selfCareDao().isCompleted(key)) {
                database.selfCareDao().insertCompletion(SelfCareCompletionEntity(key, taskId, date, System.currentTimeMillis()))
                heartsRepository.earnFromSelfCare()
            }
        }
    }

    fun completeRebuildDay(day: Int) {
        viewModelScope.launch {
            if (!database.rebuildDao().isCompleted(day)) {
                database.rebuildDao().upsert(RebuildProgressEntity(day, true, System.currentTimeMillis()))
            }
        }
    }

    fun clearAll() {
        viewModelScope.launch {
            database.clearAllTables()
            preferences.clearAll()
        }
    }

    fun onCheckIn(mood: Int, note: String) {
        viewModelScope.launch {
            journalRepository.saveEntry(mood, note.ifBlank { "Check-in" })
            heartsRepository.earnFromCheckIn()
        }
    }

    fun saveEveningReflection(gratitude: String, release: String, hope: String) {
        viewModelScope.launch {
            val content = listOf("Grateful: $gratitude", "Release: $release", "Hope: $hope").joinToString("\n")
            journalRepository.saveEntry(3, content)
        }
    }

    fun saveJournalEntry(mood: Int, content: String) {
        viewModelScope.launch {
            journalRepository.saveEntry(mood, content)
            heartsRepository.earnFromJournal()
        }
    }

    fun recordNoContactSlip() {
        viewModelScope.launch { noContactRepository.recordSlip() }
    }

    fun onRewardedHearts() {
        viewModelScope.launch {
            heartsRepository.earnFromRewardedAd()
        }
    }
}
