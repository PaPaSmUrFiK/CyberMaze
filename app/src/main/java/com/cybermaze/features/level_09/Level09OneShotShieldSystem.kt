package com.cybermaze.features.level_09

import com.cybermaze.core.game.engine.GameEventEmitter
import com.cybermaze.core.game.engine.LevelSystem
import com.cybermaze.core.game.model.CollectibleType
import com.cybermaze.core.game.model.GameState
import com.cybermaze.core.game.model.Position

/**
 * Makes shield collectible one-shot for this level:
 * the first enemy hit consumes the shield and applies normal invincibility window.
 */
class Level09OneShotShieldSystem : LevelSystem {
    private var shieldUsed = false

    override fun update(state: GameState, deltaTime: Float, emit: GameEventEmitter): GameState {
        if (!shieldUsed || !state.player.hasShield) return state
        // Force-remove stale shield flag if it lingers for any reason.
        return state.updatePlayer(state.player.removeShield())
    }

    override fun onPlayerLanded(state: GameState, emit: GameEventEmitter): GameState {
        if (shieldUsed.not()) return state
        val player = state.player
        val hasUncollectedShield = state.collectibles.any {
            it.type == CollectibleType.SHIELD && !it.isCollected
        }
        if (hasUncollectedShield.not() && player.hasShield) {
            return state.updatePlayer(player.removeShield())
        }
        return state
    }

    override fun onPlayerHit(state: GameState, emit: GameEventEmitter): GameState {
        val player = state.player
        if (!player.hasShield) return state
        shieldUsed = true
        return state.updatePlayer(
            player.removeShield().grantInvincibility()
        )
    }

    override fun onCollectibleCollected(
        state: GameState,
        type: CollectibleType,
        position: Position,
        emit: GameEventEmitter
    ): GameState {
        if (type == CollectibleType.SHIELD && shieldUsed) {
            // Do not allow re-using shield in this level after first shield break.
            return state.updatePlayer(state.player.removeShield())
        }
        return state
    }

    fun reset() {
        shieldUsed = false
    }
}
