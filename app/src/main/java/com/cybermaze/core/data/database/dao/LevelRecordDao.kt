package com.cybermaze.core.data.database.dao

import androidx.room.*
import com.cybermaze.core.data.database.entity.LevelRecordEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for level records operations.
 */
@Dao
interface LevelRecordDao {
    
    @Query("SELECT * FROM level_records WHERE levelNumber = :level ORDER BY score DESC LIMIT 10")
    fun getTopRecordsForLevel(level: Int): Flow<List<LevelRecordEntity>>
    
    @Query("SELECT * FROM level_records ORDER BY score DESC LIMIT 20")
    fun getGlobalLeaderboard(): Flow<List<LevelRecordEntity>>
    
    @Insert
    suspend fun insertRecord(record: LevelRecordEntity)
    
    @Query("DELETE FROM level_records WHERE levelNumber = :level")
    suspend fun deleteRecordsForLevel(level: Int)
    
    @Query("DELETE FROM level_records")
    suspend fun deleteAllRecords()
}
