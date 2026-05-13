package com.cybermaze.core.game.model

/**
 * Represents a position on the game grid.
 * Immutable data class for thread safety.
 */
data class Position(val x: Int, val y: Int) {
    
    /**
     * Returns a new position moved in the specified direction.
     */
    fun moved(direction: Direction): Position = when (direction) {
        Direction.UP -> copy(y = y - 1)
        Direction.DOWN -> copy(y = y + 1)
        Direction.LEFT -> copy(x = x - 1)
        Direction.RIGHT -> copy(x = x + 1)
        Direction.NONE -> this
    }
    
    /**
     * Adds two positions together.
     */
    operator fun plus(other: Position) = Position(x + other.x, y + other.y)
    
    /**
     * Calculates Manhattan distance to another position.
     */
    fun distanceTo(other: Position): Int {
        return kotlin.math.abs(x - other.x) + kotlin.math.abs(y - other.y)
    }
    
    companion object {
        val ZERO = Position(0, 0)
    }
}
