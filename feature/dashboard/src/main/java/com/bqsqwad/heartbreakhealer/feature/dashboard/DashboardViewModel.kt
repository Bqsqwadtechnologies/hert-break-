package com.bqsqwad.heartbreakhealer.feature.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bqsqwad.heartbreakhealer.core.data.repo.HeartsRepository
import com.bqsqwad.heartbreakhealer.core.data.repo.JournalRepository
import com.bqsqwad.heartbreakhealer.core.data.repo.NoContactRepository
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
    private val noContactRepository: NoContactRepository
) : ViewModel() {

    val hearts: StateFlow<Int> = heartsRepository.balance
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 50)

    val noContactDays: StateFlow<Int> = noContactRepository.currentDays
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun onCheckIn(mood: Int, note: String) {
        viewModelScope.launch {
            journalRepository.saveEntry(mood, note.ifBlank { "Check-in" })
            heartsRepository.earnFromCheckIn()
        }
    }

    fun onRewardedHearts() {
        viewModelScope.launch {
            heartsRepository.earnFromRewardedAd()
        }
    }
}
