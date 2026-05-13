package com.cybermaze.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cybermaze.core.data.database.dao.LevelRecordDao
import com.cybermaze.core.data.database.dao.PlayerProgressDao
import com.cybermaze.core.data.database.dao.SettingsDao
import com.cybermaze.core.data.database.entity.LevelRecordEntity
import com.cybermaze.core.data.database.entity.PlayerProgressEntity
import com.cybermaze.core.data.database.entity.SettingsEntity

/**
 * Main database for Cyber Maze.
 * Stores player progress, level records, and settings.
 */
@Database(
    entities = [
        PlayerProgressEntity::class,
        LevelRecordEntity::class,
        SettingsEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class CyberMazeDatabase : RoomDatabase() {
    abstract fun playerProgressDao(): PlayerProgressDao
    abstract fun levelRecordDao(): LevelRecordDao
    abstract fun settingsDao(): SettingsDao
}
