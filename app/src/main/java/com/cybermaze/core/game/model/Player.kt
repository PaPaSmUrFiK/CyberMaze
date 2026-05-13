package com.cybermaze.core.game.model

/**
 * Represents the player character.
 * Immutable data class — all updates create new instances.
 *
 * Pac-Man-style movement model:
 *   - [direction] is the current intended movement direction.
 *   - The game loop advances the player by one tile every [moveInterval] seconds
 *     (scaled by [speedMultiplier]). [moveTimer] accumulates elapsed time.
 *
 * [speedBoostSecondsLeft] — countdown from speed-boost collectible (see TZ / SpeedBoostSystem hook).
 *
 * Damage model:
 *   - On enemy contact the player loses one life (or breaks the shield) and gains
 *     a short [invincibilityTimer] window during which further hits are ignored.
 *
 * [previousPosition] — the tile the player was on before the latest [movedTo] call.
 * The renderer interpolates between [previousPosition] and [position] using
 * [moveTimer] / [effectiveStepInterval] for smooth tile-by-tile movement.
 */
data class Player(
    val position: Position,
    val lives: Int = 3,
    val score: Int = 0,
    val hasShield: Boolean = false,
    val speedMultiplier: Float = 0.5f,
    val keysCollected: Int = 0,
    val direction: Direction = Direction.NONE,
    val moveTimer: Float = 0f,
    val moveInterval: Float = DEFAULT_MOVE_INTERVAL,
    val invincibilityTimer: Float = 0f,
    /** Seconds remaining for temporary move-speed boost (collectible S). */
    val speedBoostSecondsLeft: Float = 0f,
    /** Tile the player was on before the latest step. Used by the renderer to interpolate. */
    val previousPosition: Position = position
) {
    fun isAlive(): Boolean = lives > 0

    /** True while the player cannot take damage from enemies. */
    val isInvincible: Boolean
        get() = invincibilityTimer > 0f || hasShield

    /** True when enough time has accumulated to step one tile. */
    fun canStep(): Boolean {
        val interval = effectiveStepInterval()
        return moveTimer >= interval
    }

    /** Effective seconds between auto-steps including the current [speedMultiplier]. */
    fun effectiveStepInterval(): Float = moveInterval / speedMultiplier.coerceAtLeast(0.1f)

    fun movedTo(newPosition: Position, newDirection: Direction = direction): Player =
        copy(previousPosition = position, position = newPosition, direction = newDirection)

    /** Snap the player to [newPosition] without rendering a slide (respawn / teleport). */
    fun snappedTo(newPosition: Position, newDirection: Direction = direction): Player =
        copy(previousPosition = newPosition, position = newPosition, direction = newDirection)

    fun loseLife(): Player = copy(lives = (lives - 1).coerceAtLeast(0))

    fun addScore(points: Int): Player = copy(score = score + points)

    fun collectKey(): Player = copy(keysCollected = keysCollected + 1)

    fun useKey(): Player = copy(keysCollected = (keysCollected - 1).coerceAtLeast(0))

    fun activateShield(): Player = copy(hasShield = true)

    fun removeShield(): Player = copy(hasShield = false)

    fun applySpeedBoost(multiplier: Float, durationSeconds: Float = DEFAULT_SPEED_BOOST_DURATION): Player =
        copy(speedMultiplier = multiplier, speedBoostSecondsLeft = durationSeconds)

    fun resetSpeed(): Player = copy(speedMultiplier = 1.0f, speedBoostSecondsLeft = 0f)

    fun withDirection(newDirection: Direction): Player = copy(direction = newDirection)

    fun advanceTimers(deltaTime: Float): Player {
        var p = copy(
            moveTimer = moveTimer + deltaTime,
            invincibilityTimer = (invincibilityTimer - deltaTime).coerceAtLeast(0f)
        )
        if (p.speedBoostSecondsLeft > 0f) {
            val left = (p.speedBoostSecondsLeft - deltaTime).coerceAtLeast(0f)
            p = p.copy(speedBoostSecondsLeft = left)
            if (left <= 0f) p = p.resetSpeed()
        }
        return p
    }

    fun resetMoveTimer(): Player = copy(moveTimer = 0f)

    fun grantInvincibility(seconds: Float = DEFAULT_INVINCIBILITY): Player =
        copy(invincibilityTimer = seconds)

    companion object {
        /** Default seconds between auto-steps along the current direction. */
        const val DEFAULT_MOVE_INTERVAL: Float = 0.18f

        /** Default invincibility window after a hit (seconds). */
        const val DEFAULT_INVINCIBILITY: Float = 1.5f

        const val DEFAULT_SPEED_BOOST_DURATION: Float = 3f
    }
}
