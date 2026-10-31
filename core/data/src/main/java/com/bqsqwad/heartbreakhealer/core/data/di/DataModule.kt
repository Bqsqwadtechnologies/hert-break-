package com.bqsqwad.heartbreakhealer.core.data.di

import android.content.Context
import androidx.room.Room
import com.bqsqwad.heartbreakhealer.core.data.db.AppDatabase
import com.bqsqwad.heartbreakhealer.core.data.db.JournalDao
import com.bqsqwad.heartbreakhealer.core.data.db.NoContactDao
import com.bqsqwad.heartbreakhealer.core.data.db.RebuildDao
import com.bqsqwad.heartbreakhealer.core.data.db.SelfCareDao
import com.bqsqwad.heartbreakhealer.core.data.db.TriggerDao
import com.bqsqwad.heartbreakhealer.core.data.db.UnsentLetterDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "heartbreak_healer.db")
            .fallbackToDestructiveMigration()
            .build()
    }
    @Provides fun provideJournalDao(db: AppDatabase): JournalDao = db.journalDao()
    @Provides fun provideUnsentLetterDao(db: AppDatabase): UnsentLetterDao = db.unsentLetterDao()
    @Provides fun provideTriggerDao(db: AppDatabase): TriggerDao = db.triggerDao()
    @Provides fun provideSelfCareDao(db: AppDatabase): SelfCareDao = db.selfCareDao()
    @Provides fun provideRebuildDao(db: AppDatabase): RebuildDao = db.rebuildDao()
    @Provides fun provideNoContactDao(db: AppDatabase): NoContactDao = db.noContactDao()
}
