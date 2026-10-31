package com.bqsqwad.heartbreakhealer.core.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journal_entries")
data class JournalEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val mood: Int,
    val content: String,
    val prompt: String? = null,
    val createdAt: Long
)

@Entity(tableName = "unsent_letters")
data class UnsentLetterEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val content: String,
    val createdAt: Long,
    val status: String
)

@Entity(tableName = "triggers")
data class TriggerEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val copingAction: String
)

@Entity(tableName = "self_care_completions")
data class SelfCareCompletionEntity(
    @PrimaryKey val taskIdDate: String,
    val taskId: String,
    val date: String,
    val completedAt: Long
)

@Entity(tableName = "rebuild_progress")
data class RebuildProgressEntity(
    @PrimaryKey val day: Int,
    val completed: Boolean,
    val completedAt: Long? = null
)

@Entity(tableName = "no_contact_events")
data class NoContactEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String,
    val date: String,
    val note: String? = null
)
