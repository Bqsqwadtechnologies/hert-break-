package com.bqsqwad.heartbreakhealer.feature.heartjourney

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bqsqwad.heartbreakhealer.core.data.journey.HeartJourneyRepository
import com.bqsqwad.heartbreakhealer.core.data.prefs.UserPreferences
import com.bqsqwad.heartbreakhealer.core.domain.journey.JourneyState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class HeartJourneyViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repo: HeartJourneyRepository,
    prefs: UserPreferences,
) : ViewModel() {
    /** null until the first read, so the one-time question never flashes by mistake. */
    val state: StateFlow<JourneyState?> =
        repo.state.map<JourneyState, JourneyState?> { it }.stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val reduceMotion: StateFlow<Boolean> = prefs.reduceMotion.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private var celebrationScheduled = false

    init {
        viewModelScope.launch {
            repo.state.collect { s ->
                if (s.celebrationPending && !celebrationScheduled) {
                    celebrationScheduled = true
                    JourneyScheduler.celebrate(context)
                }
                if (!s.celebrationPending) celebrationScheduled = false
            }
        }
        viewModelScope.launch {
            repo.state.map { Triple(it.joined, it.reminderEnabled, it.reminderHour) }.distinctUntilChanged()
                .collect { (joined, enabled, hour) ->
                    if (joined && enabled) JourneyScheduler.scheduleDaily(context, hour) else JourneyScheduler.cancelDaily(context)
                }
        }
    }

    fun answerBroken(yes: Boolean) = viewModelScope.launch { repo.answerBroken(yes) }
    fun startRound() = viewModelScope.launch { repo.startRound() }
    fun ackDayCounted() = viewModelScope.launch { repo.ackDayCounted() }
    fun ackCelebration() = viewModelScope.launch { repo.ackCelebration() }
    fun setReminder(enabled: Boolean, hour: Int) = viewModelScope.launch { repo.setReminder(enabled, hour) }
    fun companionShown() = viewModelScope.launch { repo.markCompanionShown() }
}
