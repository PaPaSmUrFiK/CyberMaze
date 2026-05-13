package com.cybermaze.core.data.database.dao

import androidx.room.*
import com.cybermaze.core.data.database.entity.PlayerProgressEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for player progress operations.
 */
@Dao
interface PlayerProgressDao {
    
    @Query("SELECT * FROM player_progress ORDER BY levelNumber")
    fun getAllProgress(): Flow<List<PlayerProgressEntity>>
    
    @Query("SELECT * FROM player_progress WHERE levelNumber = :level")
    suspend fun getProgressForLevel(level: Int): PlayerProgressEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProgress(progress: PlayerProgressEntity)
    
    @Query("UPDATE player_progress SET isUnlocked = 1 WHERE levelNumber = :level")
    suspend fun unlockLevel(level: Int)
    
    @Query("SELECT SUM(bestScore) FROM player_progress")
    fun getTotalScore(): Flow<Int?>
    
    @Query("SELECT COUNT(*) FROM player_progress WHERE isCompleted = 1")
    fun getCompletedLevelsCount(): Flow<Int>
    
    @Query("SELECT SUM(starsEarned) FROM player_progress")
    fun getTotalStars(): Flow<Int?>
}
