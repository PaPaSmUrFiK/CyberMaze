package com.cybermaze.features.level_07

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
 * Level 7 — "Unicorn Meadow": **no** fog / [FogOfWarSystem]; difficulty from [GuardSystem] and
 * [MovingTrapSystem] on the TZ maze.
 */
@HiltViewModel
class Level07ViewModel @Inject constructor(
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

        require(LevelLoader.validateLayout(LEVEL_07_MAP).isEmpty()) {
            "Level 07 layout invalid: ${LevelLoader.validateLayout(LEVEL_07_MAP)}"
        }

        val cfg = LEVEL_07_CONFIG
        val map = LevelLoader.loadFromStringArray(LEVEL_07_MAP, cfg)

        val systems = listOf(
            PowerUpSystem,
            SpeedBoostSystem,
            TeleportSystem,
            MovingTrapSystem(collisionSystem),
            GuardSystem(collisionSystem)
        )

        setupLevel(
            LevelDefinition(
                config = cfg,
                map = map,
                enemies = LEVEL_07_ENEMIES.map { it.copy() },
                traps = LEVEL_07_TRAPS.map { it.copy() },
                teleports = LEVEL_07_TELEPORTS.map { it.copy() },
                baseFogRadiusTiles = null,
                extraSystems = systems,
                palette = LEVEL_07_PALETTE
            )
        )
    }

    override fun createInitialState(): GameState? {
        val current = gameState.value ?: return null
        val cfg = LEVEL_07_CONFIG
        val map = LevelLoader.loadFromStringArray(LEVEL_07_MAP, cfg)
        val collectibles = collectibleSystem.createCollectiblesFromMap(map)
        val resetTraps = LEVEL_07_TRAPS.map { t ->
            val start = t.path.first()
            t.copy(position = start, pathIndex = 0, moveTimer = 0f)
        }
        return current.copy(
            player = Player(position = map.playerSpawn),
            enemies = LEVEL_07_ENEMIES.map { it.copy() },
            map = map,
            config = cfg,
            collectibles = collectibles,
            collectedPoints = 0,
            elapsedTime = 0f,
            livesLost = 0,
            phase = GamePhase.BRIEFING,
            traps = resetTraps,
            teleports = LEVEL_07_TELEPORTS.map { it.copy(cooldownA = 0f, cooldownB = 0f) },
            powerUpSecondsLeft = 0f,
            fogVisionBoostSecondsLeft = 0f,
            fogBaseRadiusTiles = null,
            palette = LEVEL_07_PALETTE
        )
    }
}
