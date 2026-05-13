package com.cybermaze.core.game.system

import com.cybermaze.core.game.engine.GameEventEmitter
import com.cybermaze.core.game.engine.LevelSystem
import com.cybermaze.core.game.model.CollectibleType
import com.cybermaze.core.game.model.GameState
import com.cybermaze.core.game.model.Position

/**
 * Slows all enemies for 5 seconds after collecting [CollectibleType.POWER_UP] (TZ level 2+).
 * Uses [com.cybermaze.core.game.model.Enemy.speedMultiplier] so authored [Enemy.speed] is preserved.
 */
object PowerUpSystem : LevelSystem {

    const val DURATION_SECONDS: Float = 5f
    const val ENEMY_SLOW_MULT: Float = 0.3f

    override fun update(state: GameState, deltaTime: Float, emit: GameEventEmitter): GameState {
        if (state.powerUpSecondsLeft <= 0f) return state
        val left = (state.powerUpSecondsLeft - deltaTime).coerceAtLeast(0f)
        var next = state.copy(powerUpSecondsLeft = left)
        if (left <= 0f) {
            next = next.updateEnemies(next.enemies.map { it.withSpeedMultiplier(1f) })
        }
        return next
    }

    override fun onCollectibleCollected(
        state: GameState,
        type: CollectibleType,
        position: Position,
        emit: GameEventEmitter
    ): GameState {
        if (type != CollectibleType.POWER_UP) return state
        return state.copy(
            powerUpSecondsLeft = DURATION_SECONDS,
            enemies = state.enemies.map { it.withSpeedMultiplier(ENEMY_SLOW_MULT) }
        )
    }
}
