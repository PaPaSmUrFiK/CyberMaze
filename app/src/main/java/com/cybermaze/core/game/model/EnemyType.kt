package com.cybermaze.core.game.model

/**
 * Types of enemy AI behavior.
 */
enum class EnemyType {
    PATROL,   // Follows a predefined patrol path
    CHASER,   // Actively chases the player
    RANDOM,   // Moves randomly
    GUARD,    // Stays in place, watches in a direction
    FAST;     // Fast random movement
    
    /**
     * Returns the default speed multiplier for this enemy type.
     */
    fun defaultSpeed(): Float = when (this) {
        PATROL -> 0.7f
        CHASER -> 0.8f
        RANDOM -> 0.6f
        GUARD -> 0.0f
        FAST -> 1.5f
    }
}
