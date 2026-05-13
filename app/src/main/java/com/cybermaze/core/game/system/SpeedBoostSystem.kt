package com.cybermaze.core.game.system

import com.cybermaze.core.game.engine.GameEventEmitter
import com.cybermaze.core.game.engine.LevelSystem
import com.cybermaze.core.game.model.GameState

/**
 * Player speed boost timing is handled inside [com.cybermaze.core.game.model.Player.advanceTimers].
 * This system exists as a named hook for level authors who want extra behaviour (FX, analytics)
 * tied to the boost window — override with a custom [LevelSystem] if needed.
 */
object SpeedBoostSystem : LevelSystem {
    override fun update(state: GameState, deltaTime: Float, emit: GameEventEmitter): GameState = state
}
