package com.bqsqwad.heartbreakhealer.core.domain.model

import java.time.LocalDate

enum class HealingStage { SHOCK, HURT, ANGER, ACCEPTANCE, GROWTH }
enum class Goal { HEAL, NO_CONTACT, REBUILD, CLOSURE }
enum class LetterStatus { DRAFT, BURNED, RELEASED, SAVED }

data class UserProfile(
    val nickname: String,
    val breakupDate: LocalDate,
    val goals: Set<Goal>,
    val onboardingComplete: Boolean = false
)

data class HeartsBalance(val balance: Int, val lifetimeEarned: Int = 0)

data class NoContactStreak(
    val currentDays: Int,
    val bestDays: Int,
    val lastSlipDate: LocalDate? = null,
    val startDate: LocalDate
)

data class JournalEntry(
    val id: Long = 0,
    val date: LocalDate,
    val mood: Int,
    val content: String,
    val prompt: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

data class SelfCareTask(
    val id: String,
    val title: String,
    val category: String,
    val completedToday: Boolean = false,
    val streak: Int = 0
)

data class RebuildDay(
    val day: Int,
    val title: String,
    val description: String,
    val category: String,
    val completed: Boolean = false
)

data class Affirmation(val id: Int, val text: String, val category: String)
data class Trigger(val id: Long = 0, val name: String, val copingAction: String)
data class UnsentLetter(
    val id: Long = 0,
    val content: String,
    val createdAt: Long,
    val status: LetterStatus = LetterStatus.DRAFT
)
