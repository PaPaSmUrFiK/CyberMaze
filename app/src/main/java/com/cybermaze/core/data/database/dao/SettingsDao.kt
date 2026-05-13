package com.cybermaze.core.data.database.dao

import androidx.room.*
import com.cybermaze.core.data.database.entity.SettingsEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for settings operations.
 */
@Dao
interface SettingsDao {
    
    @Query("SELECT * FROM settings WHERE id = 1")
    fun getSettings(): Flow<SettingsEntity?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSettings(settings: SettingsEntity)
    
    @Query("UPDATE settings SET soundEnabled = :enabled WHERE id = 1")
    suspend fun setSoundEnabled(enabled: Boolean)
    
    @Query("UPDATE settings SET vibrationEnabled = :enabled WHERE id = 1")
    suspend fun setVibrationEnabled(enabled: Boolean)
    
    @Query("UPDATE settings SET controlType = :type WHERE id = 1")
    suspend fun setControlType(type: String)
}
