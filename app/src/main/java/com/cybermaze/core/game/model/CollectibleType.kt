package com.cybermaze.core.game.model

/**
 * Types of collectible items in the game.
 */
enum class CollectibleType {
    ENERGY_POINT,
    KEY,
    POWER_UP,
    SPEED_BOOST,
    SHIELD,
    BONUS_STAR;
    
    /**
     * Returns the score value for this collectible.
     */
    fun scoreValue(): Int = when (this) {
        ENERGY_POINT -> 10
        KEY -> 50
        POWER_UP -> 100
        SPEED_BOOST -> 75
        SHIELD -> 100
        BONUS_STAR -> 200
    }
}
