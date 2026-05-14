package com.cybermaze.features.level_06

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
import com.cybermaze.core.game.system.MovementSystem
import com.cybermaze.core.game.system.TeleportSystem
import com.cybermaze.features.game.BaseLevelViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Level 06 — "Warp Zone".
 * Focuses on the [TeleportSystem] mechanic.
 */
@HiltViewModel
class Level06ViewModel @Inject constructor(
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

        require(LevelLoader.validateLayout(LEVEL_06_MAP).isEmpty()) {
            "Level 06 layout is invalid: ${LevelLoader.validateLayout(LEVEL_06_MAP)}"
        }

        val cfg = LEVEL_06_CONFIG
        val map = LevelLoader.loadFromStringArray(LEVEL_06_MAP, cfg)

        val systems = listOf(
            TeleportSystem
        )

        setupLevel(
            LevelDefinition(
                config = cfg,
                map = map,
                enemies = LEVEL_06_ENEMIES.map { it.copy() },
                teleports = LEVEL_06_TELEPORTS.map { it.copy() },
                extraSystems = systems,
                palette = LEVEL_06_PALETTE
            )
        )
    }

    override fun createInitialState(): GameState? {
        val current = gameState.value ?: return null
        val cfg = LEVEL_06_CONFIG
        val map = LevelLoader.loadFromStringArray(LEVEL_06_MAP, cfg)
        val collectibles = collectibleSystem.createCollectiblesFromMap(map)
        
        return current.copy(
            player = Player(position = map.playerSpawn),
            enemies = LEVEL_06_ENEMIES.map { it.copy() },
            map = map,
            config = cfg,
            collectibles = collectibles,
            collectedPoints = 0,
            elapsedTime = 0f,
            livesLost = 0,
            phase = GamePhase.BRIEFING,
            teleports = LEVEL_06_TELEPORTS.map { it.copy(cooldownA = 0f, cooldownB = 0f) },
            palette = LEVEL_06_PALETTE
        )
    }
}
