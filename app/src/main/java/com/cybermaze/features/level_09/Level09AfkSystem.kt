package com.cybermaze.features.level_09

import com.cybermaze.core.game.engine.GameEvent
import com.cybermaze.core.game.engine.GameEventEmitter
import com.cybermaze.core.game.engine.HazardDamage
import com.cybermaze.core.game.engine.LevelSystem
import com.cybermaze.core.game.model.GamePhase
import com.cybermaze.core.game.model.GameState
import com.cybermaze.core.game.model.Position
import com.cybermaze.core.game.system.CollisionSystem

/**
 * Anti-AFK mechanic for Level 9:
 *  - After [WARN_SECONDS] of standing still a "Move!" prompt appears on screen.
 *  - After [PENALTY_SECONDS] of standing still (warn + extra window) the player
 *    loses one life and respawns (same as an enemy hit). After the hit the
 *    invincibility window naturally prevents an instant second penalty.
 *
 * The warning is exposed via [isWarning] for [Level09Screen] to display.
 */
class Level09AfkSystem(private val collisionSystem: CollisionSystem) : LevelSystem {

    private var idleTimer: Float = 0f
    private var lastKnownPosition: Position? = null
    private var penaltyApplied: Boolean = false

    /** True while the "Move!" prompt should be visible (3 s idle → 5 s idle). */
    var isWarning: Boolean = false
        private set

    fun reset() {
        idleTimer = 0f
        lastKnownPosition = null
        isWarning = false
        penaltyApplied = false
    }

    override fun update(state: GameState, deltaTime: Float, emit: GameEventEmitter): GameState {
        if (state.phase != GamePhase.PLAYING) return state

        val currentPos = state.player.position

        // Player moved — reset everything.
        if (currentPos != lastKnownPosition) {
            lastKnownPosition = currentPos
            idleTimer = 0f
            isWarning = false
            penaltyApplied = false
            return state
        }

        idleTimer += deltaTime
        isWarning = idleTimer >= WARN_SECONDS

        // Apply penalty once when the full idle window expires.
        if (idleTimer >= PENALTY_SECONDS && !penaltyApplied && !state.player.isInvincible) {
            penaltyApplied = true
            // Timer stays at PENALTY_SECONDS — if the player still doesn't move,
            // the next tick penaltyApplied prevents a double hit. The hit grants
            // invincibility; after it expires and the player is still idle a new
            // cycle begins naturally because invincibility resets idleTimer==0? No —
            // we reset penaltyApplied when player moves. So if they stand still after
            // invincibility ends we need another reset cycle. Reset idleTimer so the
            // next penalty requires another full PENALTY_SECONDS of standing.
            idleTimer = 0f
            isWarning = false
            val beforeLives = state.player.lives
            val next = HazardDamage.applyPlayerHit(state, collisionSystem)
            emit(GameEvent.PlayerHit(next.player.lives))
            if (next.player.lives < beforeLives) {
                if (next.player.lives <= 0) emit(GameEvent.PlayerDied) else emit(GameEvent.PlayerRespawned)
            }
            return next
        }

        return state
    }

    companion object {
        const val WARN_SECONDS: Float = 3f
        const val PENALTY_SECONDS: Float = 5f
    }
}
