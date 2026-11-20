package com.bqsqwad.heartbreakhealer.feature.dashboard

import com.bqsqwad.heartbreakhealer.core.data.journey.HeartJourneyRepository
import com.bqsqwad.heartbreakhealer.core.data.journey.JourneyEvents

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
    private val database: AppDatabase,
    private val journey: HeartJourneyRepository
) : ViewModel() {

    val onboardingComplete = preferences.onboardingComplete
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val hearts: StateFlow<Int> = heartsRepository.balance
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 50)

    val noContactDays: StateFlow<Int> = noContactRepository.currentDays
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val bestNoContactDays: StateFlow<Int> = noContactRepository.bestDays
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val unsentLetters = journalRepository.getLetters()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val journalEntries = journalRepository.getEntries()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val selfCareCompleted = database.selfCareDao().getCompletionsForDate(LocalDate.now().toString())
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val rebuildProgress = database.rebuildDao().getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val rebuildCompletedDays = preferences.completedRebuildDays
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())
    val lastJourneyCompletionDate = preferences.lastJourneyCompletionDate
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    val urgePassedCount = preferences.urgePassedCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val reduceMotion = preferences.reduceMotion
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    val memories = preferences.savedMemories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())
    val completedPathDays = preferences.completedPathDays
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())
    val viewedStoryIds = preferences.viewedStoryIds
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())
    val seenJokeIds = preferences.seenJokeIds
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())
    val activeStoryId = preferences.activeStoryId
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    val activeStoryPart = preferences.activeStoryPart
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val activeJokeIndex = preferences.activeJokeIndex
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), -1)
    val triggers = preferences.savedTriggers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())
    val dailyRemindersEnabled = preferences.dailyRemindersEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    val unlockedSoundscapes = preferences.unlockedSoundscapes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())
    val unlockedBreathworkDeep = preferences.unlockedBreathworkDeep
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val capsuleText: StateFlow<String?> = preferences.timeCapsuleText
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    val capsuleUnlock: StateFlow<String?> = preferences.timeCapsuleUnlock
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun completeOnboarding(nickname: String, breakupDate: LocalDate, goals: Set<com.bqsqwad.heartbreakhealer.core.domain.model.Goal>) {
        viewModelScope.launch {
            preferences.setOnboardingComplete(nickname, goals.map { it.name }.toSet())
            preferences.setBreakupEpoch(breakupDate.toEpochDay())
            preferences.setNoContactStartEpoch(LocalDate.now().toEpochDay())
        }
    }

    fun saveCapsule(text: String, days: Int) {
        viewModelScope.launch {
            preferences.setTimeCapsule(text, LocalDate.now().plusDays(days.toLong()).toString())
            journey.recordEvent(JourneyEvents.CAPSULE)
        }
    }

    fun saveUnsentLetter(content: String) { viewModelScope.launch { journalRepository.saveLetter(content); journey.recordEvent(JourneyEvents.LETTER) } }
    fun releaseUnsentLetter(id: Long) { viewModelScope.launch { journalRepository.releaseLetter(id) } }

    fun completePathDay(day: Int) { viewModelScope.launch { preferences.completePathDay(day) } }

    fun markStoryViewed(storyId: String) { viewModelScope.launch { preferences.markStoryViewed(storyId); journey.recordStory() } }
    fun markJokeSeen(jokeId: String) { viewModelScope.launch { preferences.markJokeSeen(jokeId); journey.recordJoke(jokeId) } }
    fun saveStoryProgress(storyId: String, partIndex: Int) {
        viewModelScope.launch { preferences.saveStoryProgress(storyId, partIndex); if (partIndex > 0) journey.recordStory() }
    }
    fun clearStoryProgress() { viewModelScope.launch { preferences.clearStoryProgress() } }
    fun saveJokeProgress(index: Int) { viewModelScope.launch { preferences.saveJokeProgress(index) } }
    fun clearJokeProgress() { viewModelScope.launch { preferences.clearJokeProgress() } }

    fun logUrgePassed() { viewModelScope.launch { preferences.incrementUrgePassed(); journey.recordEvent(JourneyEvents.URGE) } }

    fun addMemory(title: String, note: String) { viewModelScope.launch { preferences.addMemory(title, note); journey.recordEvent(JourneyEvents.MEMORY) } }
    fun addTrigger(name: String, coping: String) { viewModelScope.launch { preferences.addTrigger(name, coping); journey.recordEvent(JourneyEvents.TRIGGER) } }

    fun setReduceMotion(enabled: Boolean) { viewModelScope.launch { preferences.setReduceMotion(enabled) } }
    fun setDailyRemindersEnabled(enabled: Boolean) { viewModelScope.launch { preferences.setDailyRemindersEnabled(enabled) } }
    fun unlockSoundscape(id: String) { viewModelScope.launch { preferences.unlockSoundscape(id) } }
    fun unlockBreathworkDeep() { viewModelScope.launch { preferences.unlockBreathworkDeep() } }

    fun completeSelfCare(taskId: String) {
        viewModelScope.launch {
            val date = LocalDate.now().toString()
            val key = "$date:$taskId"
            if (!database.selfCareDao().isCompleted(key)) {
                database.selfCareDao().insertCompletion(SelfCareCompletionEntity(key, taskId, date, System.currentTimeMillis()))
                heartsRepository.earnFromSelfCare()
                journey.recordEvent(JourneyEvents.SELFCARE)
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

    fun completeJourneyDay(
        journey: String,
        day: Int,
        alreadyCompletedDays: Set<Int>,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            val completed = preferences.tryCompleteJourneyDay(
                journey = journey,
                day = day,
                alreadyCompletedDays = alreadyCompletedDays,
                today = LocalDate.now().toString()
            )
            onResult(completed)
        }
    }

    fun clearAll() {
        viewModelScope.launch {
            database.clearAllTables()
            preferences.clearAll()
            journey.clearAll()
        }
    }

    fun onCheckIn(mood: Int, note: String) {
        viewModelScope.launch {
            journalRepository.saveEntry(mood, note.ifBlank { "Check-in" })
            heartsRepository.earnFromCheckIn()
            journey.recordEvent(JourneyEvents.CHECKIN)
        }
    }

    fun saveEveningReflection(gratitude: String, release: String, hope: String) {
        viewModelScope.launch {
            val content = listOf("Grateful: $gratitude", "Release: $release", "Hope: $hope").joinToString("\n")
            journalRepository.saveEntry(3, content)
            journey.recordEvent(JourneyEvents.EVENING)
        }
    }

    fun saveJournalEntry(mood: Int, content: String) {
        viewModelScope.launch {
            journalRepository.saveEntry(mood, content)
            heartsRepository.earnFromJournal()
            journey.recordEvent(JourneyEvents.JOURNAL)
        }
    }

    fun recordNoContactSlip() {
        viewModelScope.launch { noContactRepository.recordSlip() }
    }

    fun spendHearts(amount: Int, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            onResult(heartsRepository.spend(amount))
        }
    }

    fun onRewardedHearts(onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            heartsRepository.earnFromRewardedAd()
            onComplete()
        }
    }

    private val trackedVisits = setOf(
        "affirmations", "coping", "boundaries", "wins", "grief_contact", "library", "weekly", "no_contact", "sos", "soundscapes", "breathwork",
    )
    fun recordVisit(route: String?) {
        if (route != null && route in trackedVisits) viewModelScope.launch { journey.recordEvent(JourneyEvents.visit(route)) }
    }

}
