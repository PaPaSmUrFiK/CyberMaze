package com.cybermaze.core.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity for storing player progress for each level.
 */
@Entity(tableName = "player_progress")
data class PlayerProgressEntity(
    @PrimaryKey val levelNumber: Int,
    val isUnlocked: Boolean = false,
    val isCompleted: Boolean = false,
    val starsEarned: Int = 0,            // 0-3 stars
    val bestScore: Int = 0,
    val bestTimeSeconds: Float = 0f,
    val totalAttempts: Int = 0,
    val lastPlayedAt: Long = 0L          // timestamp
)
