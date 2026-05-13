package com.cybermaze.core.game.model

/**
 * Configuration for a game level.
 * Defines level properties and win conditions.
 */
data class LevelConfig(
    val levelNumber: Int,
    val title: String,
    val description: String,
    val timeLimit: Int? = null,         // Time limit in seconds, null = no limit
    val requiredPoints: Int,            // Minimum points needed to win
    val totalPoints: Int,               // Total points available on the level
    val hasKeys: Boolean = false,
    val hasTeleports: Boolean = false,
    val hasDarkness: Boolean = false,
    val hasMovingTraps: Boolean = false,
    val specialMechanic: String? = null
) {
    /**
     * Returns true if the level has a time limit.
     */
    fun isTimed(): Boolean = timeLimit != null
    
    /**
     * Calculates the percentage of points collected.
     */
    fun calculateProgress(collectedPoints: Int): Float {
        if (totalPoints == 0) return 0f
        return (collectedPoints.toFloat() / totalPoints.toFloat()).coerceIn(0f, 1f)
    }
    
    /**
     * Returns true if enough points have been collected to win.
     */
    fun hasEnoughPoints(collectedPoints: Int): Boolean {
        return collectedPoints >= requiredPoints
    }
}
