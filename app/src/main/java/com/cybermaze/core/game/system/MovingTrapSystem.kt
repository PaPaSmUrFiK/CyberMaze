package com.cybermaze.core.game.system

import com.cybermaze.core.game.engine.GameEvent
import com.cybermaze.core.game.engine.GameEventEmitter
import com.cybermaze.core.game.engine.HazardDamage
import com.cybermaze.core.game.engine.LevelSystem
import com.cybermaze.core.game.model.CollectibleType
import com.cybermaze.core.game.model.GameState
import com.cybermaze.core.game.model.Position

/**
 * Advances [GameState.traps] on a timer and applies the same damage rules as enemy contact.
 */
class MovingTrapSystem(
    private val collisionSystem: CollisionSystem
) : LevelSystem {

    override fun update(state: GameState, deltaTime: Float, emit: GameEventEmitter): GameState {
        if (state.traps.isEmpty()) return state

        val newTraps = state.traps.map { trap ->
            var t = trap.advanceTimer(deltaTime)
            if (t.canStep()) {
                t = t.stepForward()
            }
            t
        }

        var next = state.updateTraps(newTraps)
        val hitTrap = newTraps.any { it.position == next.player.position }
        if (hitTrap && !next.player.isInvincible) {
            val beforeLives = next.player.lives
            next = HazardDamage.applyPlayerHit(next, collisionSystem)
            emit(GameEvent.HazardHit)
            emit(GameEvent.PlayerHit(next.player.lives))
            if (next.player.lives < beforeLives) {
                if (next.player.lives <= 0) emit(GameEvent.PlayerDied) else emit(GameEvent.PlayerRespawned)
            }
        }
        return next
    }

    override fun onCollectibleCollected(
        state: GameState,
        type: CollectibleType,
        position: Position,
        emit: GameEventEmitter
    ): GameState = state
}
