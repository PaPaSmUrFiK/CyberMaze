package com.cybermaze.core.game.system

import com.cybermaze.core.game.engine.GameEvent
import com.cybermaze.core.game.engine.GameEventEmitter
import com.cybermaze.core.game.engine.LevelSystem
import com.cybermaze.core.game.model.CollectibleType
import com.cybermaze.core.game.model.GameState
import com.cybermaze.core.game.model.Position

/**
 * Warps the player between paired teleport tiles (TZ level 6+). Cooldown prevents ping-pong.
 */
object TeleportSystem : LevelSystem {

    override fun update(state: GameState, deltaTime: Float, emit: GameEventEmitter): GameState {
        if (state.teleports.isEmpty()) return state
        return state.updateTeleports(
            state.teleports.map { it.tickCooldowns(deltaTime) }
        )
    }

    override fun onPlayerLanded(state: GameState, emit: GameEventEmitter): GameState {
        if (state.teleports.isEmpty()) return state
        val pos = state.player.position
        var player = state.player
        var teleports = state.teleports
        var changed = false

        teleports = teleports.map { tp ->
            when {
                pos == tp.posA && tp.cooldownA <= 0f -> {
                    player = player.snappedTo(tp.posB, player.direction).resetMoveTimer()
                    changed = true
                    emit(GameEvent.PlayerTeleported(tp.posB))
                    tp.triggerFromA()
                }
                pos == tp.posB && tp.cooldownB <= 0f -> {
                    player = player.snappedTo(tp.posA, player.direction).resetMoveTimer()
                    changed = true
                    emit(GameEvent.PlayerTeleported(tp.posA))
                    tp.triggerFromB()
                }
                else -> tp
            }
        }

        return if (changed) state.updatePlayer(player).updateTeleports(teleports) else state
    }

    override fun onCollectibleCollected(
        state: GameState,
        type: CollectibleType,
        position: Position,
        emit: GameEventEmitter
    ): GameState = state
}
