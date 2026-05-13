package com.cybermaze.core.game.engine

import com.cybermaze.core.game.model.CollectibleType
import com.cybermaze.core.game.model.Position

/**
 * Discrete events emitted by the game loop. Consumers (audio/haptics/UI)
 * subscribe to react without polling game state.
 */
sealed class GameEvent {
    data class PlayerMoved(val newPosition: Position) : GameEvent()
    data class EnemyMoved(val enemyId: String) : GameEvent()
    data class CollectibleCollected(val type: CollectibleType, val position: Position) : GameEvent()
    data class PlayerHit(val livesRemaining: Int) : GameEvent()
    data class DoorOpened(val position: Position) : GameEvent()
    data class LevelCompleted(val score: Int, val stars: Int) : GameEvent()
    data class LevelFailed(val reason: String) : GameEvent()
    data object PlayerDied : GameEvent()
    data object GamePaused : GameEvent()
    data object GameResumed : GameEvent()
    data object PlayerRespawned : GameEvent()

    /** Player used a teleport pad (for VFX / SFX). */
    data class PlayerTeleported(val to: Position) : GameEvent()

    /** Moving trap or guard vision dealt damage (for VFX / SFX). */
    data object HazardHit : GameEvent()
}
