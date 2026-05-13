package com.cybermaze.core.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity for storing game settings.
 * Singleton entity (only one row with id=1).
 */
@Entity(tableName = "settings")
data class SettingsEntity(
    @PrimaryKey val id: Int = 1,         // Singleton
    val soundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val controlType: String = "SWIPE",   // SWIPE | DPAD
    val musicVolume: Float = 0.7f,
    val sfxVolume: Float = 1.0f
)
