package com.cybermaze.features.level_10

import androidx.lifecycle.viewModelScope
import com.cybermaze.core.data.repository.ProgressRepository
import com.cybermaze.core.game.engine.LevelDefinition
import com.cybermaze.core.game.level.LevelLoader
import com.cybermaze.core.game.model.GamePhase
import com.cybermaze.core.game.model.GameState
import com.cybermaze.core.game.model.Player
import com.cybermaze.core.game.system.CollectibleSystem
import com.cybermaze.core.game.system.CollisionSystem
import com.cybermaze.core.game.system.EnemySystem
import com.cybermaze.core.game.system.FogOfWarSystem
import com.cybermaze.core.game.system.GuardSystem
import com.cybermaze.core.game.system.MovementSystem
import com.cybermaze.core.game.system.MovingTrapSystem
import com.cybermaze.core.game.system.PowerUpSystem
import com.cybermaze.core.game.system.SpeedBoostSystem
import com.cybermaze.core.game.system.TeleportSystem
import com.cybermaze.features.game.BaseLevelViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Level 10 — "Final Core".
 *
 * Wires **every** [com.cybermaze.core.game.engine.LevelSystem] plugin and the full set of
 * overlay layers from the template package — this is the boss level. Mirrors the
 * `LevelTemplateViewModel` setup but with concrete data for the final stage.
 */
@HiltViewModel
class Level10ViewModel @Inject constructor(
    progressRepository: ProgressRepository,
    movementSystem: MovementSystem,
    collisionSystem: CollisionSystem,
    enemySystem: EnemySystem,
    collectibleSystem: CollectibleSystem
) : BaseLevelViewModel(
    progressRepository,
    movementSystem,
    collisionSystem,
    enemySystem,
    collectibleSystem
) {

    init {
        viewModelScope.launch { progressRepository.initializeProgress() }

        require(LevelLoader.validateLayout(LEVEL_10_MAP).isEmpty()) {
            "Level 10 layout is invalid: ${LevelLoader.validateLayout(LEVEL_10_MAP)}"
        }

        val cfg = LEVEL_10_CONFIG
        val map = LevelLoader.loadFromStringArray(LEVEL_10_MAP, cfg)

        // Order matters when two systems listen to the same collectible
        // (e.g. POWER_UP — keep PowerUpSystem before FogOfWarSystem).
        val systems = listOf(
            PowerUpSystem,
            SpeedBoostSystem,
            TeleportSystem,
            MovingTrapSystem(collisionSystem),
            GuardSystem(collisionSystem),
            FogOfWarSystem
        )

        setupLevel(
            LevelDefinition(
                config = cfg,
                map = map,
                enemies = LEVEL_10_ENEMIES.map { it.copy() },
                traps = LEVEL_10_TRAPS.map { it.copy() },
                teleports = LEVEL_10_TELEPORTS.map { it.copy() },
                baseFogRadiusTiles = LEVEL_10_FOG_RADIUS,
                extraSystems = systems,
                palette = LEVEL_10_PALETTE
            )
        )
    }

    override fun createInitialState(): GameState? {
        val current = gameState.value ?: return null
        val cfg = LEVEL_10_CONFIG
        val map = LevelLoader.loadFromStringArray(LEVEL_10_MAP, cfg)
        val collectibles = collectibleSystem.createCollectiblesFromMap(map)
        val resetTraps = LEVEL_10_TRAPS.map { t ->
            val start = t.path.firstOrNull() ?: t.position
            t.copy(position = start, pathIndex = 0, moveTimer = 0f)
        }
        return current.copy(
            player = Player(position = map.playerSpawn),
            enemies = LEVEL_10_ENEMIES.map { it.copy() },
            map = map,
            config = cfg,
            collectibles = collectibles,
            collectedPoints = 0,
            elapsedTime = 0f,
            livesLost = 0,
            phase = GamePhase.BRIEFING,
            traps = resetTraps,
            teleports = LEVEL_10_TELEPORTS.map { it.copy(cooldownA = 0f, cooldownB = 0f) },
            powerUpSecondsLeft = 0f,
            fogVisionBoostSecondsLeft = 0f,
            fogBaseRadiusTiles = LEVEL_10_FOG_RADIUS,
            palette = LEVEL_10_PALETTE
        )
    }

    companion object {
        /** Initial fog-of-war visibility radius around the player (in tiles). */
        const val LEVEL_10_FOG_RADIUS: Int = 4
    }
}
