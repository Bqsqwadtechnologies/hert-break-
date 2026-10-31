package com.bqsqwad.heartbreakhealer.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        JournalEntryEntity::class,
        UnsentLetterEntity::class,
        TriggerEntity::class,
        SelfCareCompletionEntity::class,
        RebuildProgressEntity::class,
        NoContactEventEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun journalDao(): JournalDao
    abstract fun unsentLetterDao(): UnsentLetterDao
    abstract fun triggerDao(): TriggerDao
    abstract fun selfCareDao(): SelfCareDao
    abstract fun rebuildDao(): RebuildDao
    abstract fun noContactDao(): NoContactDao
}
