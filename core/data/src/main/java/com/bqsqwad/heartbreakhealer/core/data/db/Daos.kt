package com.bqsqwad.heartbreakhealer.core.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao {
    @Query("SELECT * FROM journal_entries ORDER BY createdAt DESC")
    fun getAll(): Flow<List<JournalEntryEntity>>
    @Query("SELECT * FROM journal_entries WHERE date = :date LIMIT 1")
    suspend fun getByDate(date: String): JournalEntryEntity?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: JournalEntryEntity): Long
    @Query("DELETE FROM journal_entries WHERE id = :id")
    suspend fun delete(id: Long)
}

@Dao
interface UnsentLetterDao {
    @Query("SELECT * FROM unsent_letters ORDER BY createdAt DESC")
    fun getAll(): Flow<List<UnsentLetterEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(letter: UnsentLetterEntity): Long
    @Query("UPDATE unsent_letters SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Long, status: String)
    @Query("DELETE FROM unsent_letters WHERE id = :id")
    suspend fun delete(id: Long)
}

@Dao
interface TriggerDao {
    @Query("SELECT * FROM triggers ORDER BY name")
    fun getAll(): Flow<List<TriggerEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(trigger: TriggerEntity): Long
    @Query("DELETE FROM triggers WHERE id = :id")
    suspend fun delete(id: Long)
}

@Dao
interface SelfCareDao {
    @Query("SELECT * FROM self_care_completions WHERE date = :date")
    fun getCompletionsForDate(date: String): Flow<List<SelfCareCompletionEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompletion(completion: SelfCareCompletionEntity)
}

@Dao
interface RebuildDao {
    @Query("SELECT * FROM rebuild_progress")
    fun getAll(): Flow<List<RebuildProgressEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(progress: RebuildProgressEntity)
}

@Dao
interface NoContactDao {
    @Query("SELECT * FROM no_contact_events ORDER BY date DESC")
    fun getAll(): Flow<List<NoContactEventEntity>>
    @Insert
    suspend fun insert(event: NoContactEventEntity): Long
}
