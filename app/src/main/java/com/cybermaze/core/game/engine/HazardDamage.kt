package com.cybermaze.core.game.engine

import com.cybermaze.core.game.model.Direction
import com.cybermaze.core.game.model.GameState
import com.cybermaze.core.game.system.CollisionSystem

/**
 * Shared "touch hazard" resolution: same rules as walking into an enemy
 * (shield / invulnerability / lose life / respawn at spawn).
 */
object HazardDamage {

    fun applyPlayerHit(state: GameState, collisionSystem: CollisionSystem): GameState {
        val player = state.player
        if (player.isInvincible) return state
        val afterHit = collisionSystem.handleEnemyCollision(player)
        val livesLostNow = if (afterHit.lives < player.lives) state.livesLost + 1 else state.livesLost
        return if (afterHit.lives <= 0) {
            state.updatePlayer(afterHit).copy(livesLost = livesLostNow)
        } else {
            val respawned = afterHit.copy(
                previousPosition = state.map.playerSpawn,
                position = state.map.playerSpawn,
                direction = Direction.NONE,
                moveTimer = 0f
            )
            state.updatePlayer(respawned).copy(livesLost = livesLostNow)
        }
    }
}
