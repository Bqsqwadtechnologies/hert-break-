package com.bqsqwad.heartbreakhealer.core.data.repo

import com.bqsqwad.heartbreakhealer.core.data.db.JournalDao
import com.bqsqwad.heartbreakhealer.core.data.db.JournalEntryEntity
import com.bqsqwad.heartbreakhealer.core.data.db.UnsentLetterDao
import com.bqsqwad.heartbreakhealer.core.data.db.UnsentLetterEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JournalRepository @Inject constructor(
    private val journalDao: JournalDao,
    private val letterDao: UnsentLetterDao
) {
    fun getEntries(): Flow<List<JournalEntryEntity>> = journalDao.getAll()

    suspend fun saveEntry(mood: Int, content: String) {
        journalDao.insert(
            JournalEntryEntity(
                date = LocalDate.now().toString(),
                mood = mood.coerceIn(1, 5),
                content = content,
                createdAt = System.currentTimeMillis()
            )
        )
    }

    fun getLetters(): Flow<List<UnsentLetterEntity>> = letterDao.getAll()

    suspend fun saveLetter(content: String) {
        letterDao.insert(
            UnsentLetterEntity(
                content = content,
                createdAt = System.currentTimeMillis(),
                status = "DRAFT"
            )
        )
    }

    suspend fun releaseLetter(id: Long) {
        letterDao.updateStatus(id, "RELEASED")
    }
}
