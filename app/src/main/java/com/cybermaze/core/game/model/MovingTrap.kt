package com.cybermaze.core.game.model

/**
 * A trap that moves along [path] every [moveInterval] seconds.
 * Used on levels 5, 8, 9, 10 per TZ.
 */
data class MovingTrap(
    val id: String,
    val position: Position,
    val path: List<Position>,
    val pathIndex: Int = 0,
    val moveInterval: Float = 0.8f,
    val moveTimer: Float = 0f
) {
    fun advanceTimer(delta: Float): MovingTrap = copy(moveTimer = moveTimer + delta)

    fun resetTimer(): MovingTrap = copy(moveTimer = 0f)

    fun canStep(): Boolean = moveTimer >= moveInterval

    fun stepForward(): MovingTrap {
        if (path.isEmpty()) return this
        val next = (pathIndex + 1) % path.size
        return copy(
            pathIndex = next,
            position = path[next],
            moveTimer = 0f
        )
    }
}
