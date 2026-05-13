package com.cybermaze.core.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity for storing level completion records.
 * Keeps history of all attempts.
 */
@Entity(tableName = "level_records")
data class LevelRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val levelNumber: Int,
    val score: Int,
    val timeSeconds: Float,
    val starsEarned: Int,
    val achievedAt: Long = System.currentTimeMillis()
)
