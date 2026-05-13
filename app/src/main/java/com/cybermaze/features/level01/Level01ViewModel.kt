package com.cybermaze.features.level01

import androidx.lifecycle.viewModelScope
import com.cybermaze.core.data.repository.ProgressRepository
import com.cybermaze.core.game.engine.LevelDefinition
import com.cybermaze.core.game.level.LevelLoader
import com.cybermaze.core.game.model.GameState
import com.cybermaze.core.game.model.Player
import com.cybermaze.core.game.system.CollectibleSystem
import com.cybermaze.core.game.system.CollisionSystem
import com.cybermaze.core.game.system.EnemySystem
import com.cybermaze.core.game.system.MovementSystem
import com.cybermaze.features.game.BaseLevelViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Level 01 — "Boot Sector".
 *
 * Reference implementation: subclasses for levels 2–10 should follow the
 * exact same pattern (load map, override [createInitialState] for proper
 * reset, optionally override [onRestart] for level-specific logic).
 */
@HiltViewModel
class Level01ViewModel @Inject constructor(
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
        // Make sure progress rows exist so the level select can show the unlock state.
        viewModelScope.launch { progressRepository.initializeProgress() }

        // Validate the layout in debug builds so an invalid map crashes loudly.
        require(LevelLoader.validateLayout(LEVEL_01_MAP).isEmpty()) {
            "Level 01 layout is invalid: ${LevelLoader.validateLayout(LEVEL_01_MAP)}"
        }

        val map = LevelLoader.loadFromStringArray(LEVEL_01_MAP, LEVEL_01_CONFIG)
        setupLevel(
            LevelDefinition(
                config = LEVEL_01_CONFIG,
                map = map,
                enemies = LEVEL_01_ENEMIES.map { it.copy() }
            )
        )
    }

    override fun createInitialState(): GameState? {
        val current = gameState.value ?: return null
        val map = LevelLoader.loadFromStringArray(LEVEL_01_MAP, LEVEL_01_CONFIG)
        val collectibles = collectibleSystem.createCollectiblesFromMap(map)
        return current.copy(
            player = Player(position = map.playerSpawn),
            enemies = LEVEL_01_ENEMIES.map { it.copy() },
            map = map,
            config = LEVEL_01_CONFIG,
            collectibles = collectibles,
            collectedPoints = 0,
            elapsedTime = 0f,
            livesLost = 0,
            traps = emptyList(),
            teleports = emptyList(),
            powerUpSecondsLeft = 0f,
            fogVisionBoostSecondsLeft = 0f,
            fogBaseRadiusTiles = null,
            phase = com.cybermaze.core.game.model.GamePhase.PLAYING
        )
    }
}
