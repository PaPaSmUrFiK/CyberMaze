package com.cybermaze.features.level_04

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
import com.cybermaze.features.game.BaseLevelViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Level 04 — "Locked Grid".
 *
 * This level uses the base engine's key/door mechanics.
 */
@HiltViewModel
class Level04ViewModel @Inject constructor(
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

        require(LevelLoader.validateLayout(LEVEL_04_MAP).isEmpty()) {
            "Level 04 layout is invalid: ${LevelLoader.validateLayout(LEVEL_04_MAP)}"
        }

        val map = LevelLoader.loadFromStringArray(LEVEL_04_MAP, LEVEL_04_CONFIG)
        setupLevel(
            LevelDefinition(
                config = LEVEL_04_CONFIG,
                map = map,
                enemies = LEVEL_04_ENEMIES.map { it.copy() }
            )
        )
    }

    override fun createInitialState(): GameState? {
        val current = gameState.value ?: return null
        val map = LevelLoader.loadFromStringArray(LEVEL_04_MAP, LEVEL_04_CONFIG)
        val collectibles = collectibleSystem.createCollectiblesFromMap(map)
        return current.copy(
            player = Player(position = map.playerSpawn),
            enemies = LEVEL_04_ENEMIES.map { it.copy() },
            map = map,
            config = LEVEL_04_CONFIG,
            collectibles = collectibles,
            collectedPoints = 0,
            elapsedTime = 0f,
            livesLost = 0,
            phase = GamePhase.BRIEFING,
            traps = emptyList(),
            teleports = emptyList(),
            powerUpSecondsLeft = 0f,
            fogVisionBoostSecondsLeft = 0f,
            fogBaseRadiusTiles = null
        )
    }
}
