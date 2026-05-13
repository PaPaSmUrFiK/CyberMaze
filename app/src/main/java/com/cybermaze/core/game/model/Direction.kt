package com.cybermaze.core.game.model

/**
 * Represents movement direction in the game.
 */
enum class Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT,
    NONE;
    
    /**
     * Returns the opposite direction.
     */
    fun opposite(): Direction = when (this) {
        UP -> DOWN
        DOWN -> UP
        LEFT -> RIGHT
        RIGHT -> LEFT
        NONE -> NONE
    }
    
    /**
     * Returns true if this is a valid movement direction (not NONE).
     */
    fun isValid(): Boolean = this != NONE
}
