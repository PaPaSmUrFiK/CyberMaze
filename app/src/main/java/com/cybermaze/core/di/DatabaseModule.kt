package com.cybermaze.core.di

import android.content.Context
import androidx.room.Room
import com.cybermaze.core.data.database.CyberMazeDatabase
import com.cybermaze.core.data.database.dao.LevelRecordDao
import com.cybermaze.core.data.database.dao.PlayerProgressDao
import com.cybermaze.core.data.database.dao.SettingsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for database dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): CyberMazeDatabase {
        return Room.databaseBuilder(
            context,
            CyberMazeDatabase::class.java,
            "cybermaze.db"
        ).build()
    }
    
    @Provides
    fun providePlayerProgressDao(database: CyberMazeDatabase): PlayerProgressDao {
        return database.playerProgressDao()
    }
    
    @Provides
    fun provideLevelRecordDao(database: CyberMazeDatabase): LevelRecordDao {
        return database.levelRecordDao()
    }
    
    @Provides
    fun provideSettingsDao(database: CyberMazeDatabase): SettingsDao {
        return database.settingsDao()
    }
}
