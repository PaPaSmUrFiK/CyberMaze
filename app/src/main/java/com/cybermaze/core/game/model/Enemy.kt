package com.cybermaze.core.game.model

/**
 * Represents an enemy in the game.
 * Immutable — all updates create new instances.
 *
 * Movement is timed: [moveTimer] accumulates elapsed seconds; the enemy
 * takes a step once it exceeds 1 / (speed * speedMultiplier) seconds.
 *
 * [speedMultiplier] is used by [com.cybermaze.core.game.system.PowerUpSystem] to slow
 * all enemies without mutating their authored [speed] value.
 *
 * For [EnemyType.PATROL] the [patrolPath] is cycled in order via [patrolIndex].
 * For [EnemyType.GUARD] [lookDirection] holds the watched direction.
 */
data class Enemy(
    val id: String,
    val position: Position,
    val type: EnemyType,
    val speed: Float = type.defaultSpeed(),
    val direction: Direction = Direction.RIGHT,
    val isActive: Boolean = true,
    val patrolPath: List<Position> = emptyList(),
    val patrolIndex: Int = 0,
    val moveTimer: Float = 0f,
    val lookDirection: Direction = Direction.RIGHT,
    val rotationTimer: Float = 0f,
    val speedMultiplier: Float = 1f,
    /** Tile the enemy was on before the latest step. Used by the renderer to interpolate. */
    val previousPosition: Position = position
) {
    fun movedTo(newPosition: Position, newDirection: Direction = direction): Enemy =
        copy(previousPosition = position, position = newPosition, direction = newDirection)

    /** Advance to the next position in [patrolPath] without recomputing timers. */
    fun advancePatrol(): Enemy {
        if (patrolPath.isEmpty()) return this
        val nextIndex = (patrolIndex + 1) % patrolPath.size
        return copy(
            previousPosition = position,
            patrolIndex = nextIndex,
            position = patrolPath[nextIndex]
        )
    }

    /** Seconds between steps with current speed multiplier. */
    fun effectiveStepInterval(): Float {
        val effective = speed * speedMultiplier.coerceAtLeast(0f)
        return if (effective <= 0f) Float.POSITIVE_INFINITY else 1f / effective
    }

    fun updateTimer(deltaTime: Float): Enemy = copy(moveTimer = moveTimer + deltaTime)

    fun resetTimer(): Enemy = copy(moveTimer = 0f)

    /** True when [moveTimer] has accumulated enough time to act based on effective speed. */
    fun canMove(): Boolean {
        val effective = speed * speedMultiplier.coerceAtLeast(0f)
        if (effective <= 0f) return false
        val moveInterval = 1.0f / effective
        return moveTimer >= moveInterval
    }

    fun setActive(active: Boolean): Enemy = copy(isActive = active)

    fun withSpeed(newSpeed: Float): Enemy = copy(speed = newSpeed.coerceAtLeast(0f))

    fun withSpeedMultiplier(mult: Float): Enemy = copy(speedMultiplier = mult.coerceAtLeast(0f))
}
