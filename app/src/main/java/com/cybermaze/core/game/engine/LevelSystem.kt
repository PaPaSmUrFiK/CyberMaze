package com.cybermaze.core.game.engine

import com.cybermaze.core.game.model.CollectibleType
import com.cybermaze.core.game.model.GameState
import com.cybermaze.core.game.model.Position

/** Emits discrete [GameEvent]s from pluggable [LevelSystem] implementations. */
typealias GameEventEmitter = (GameEvent) -> Unit

/**
 * Optional per-level logic plugged into [com.cybermaze.features.game.BaseLevelViewModel].
 * Default implementations are no-ops so levels only override what they need.
 *
 * [emit] is always provided by the host ViewModel; systems may ignore it.
 */
interface LevelSystem {
    fun update(state: GameState, deltaTime: Float, emit: GameEventEmitter): GameState = state

    fun onPlayerLanded(state: GameState, emit: GameEventEmitter): GameState = state

    fun onCollectibleCollected(
        state: GameState,
        type: CollectibleType,
        position: Position,
        emit: GameEventEmitter
    ): GameState = state

    fun onPlayerHit(state: GameState, emit: GameEventEmitter): GameState = state
}
