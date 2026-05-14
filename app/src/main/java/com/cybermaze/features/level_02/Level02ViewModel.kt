package com.cybermaze.features.level_02

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
import com.cybermaze.core.game.system.PowerUpSystem
import com.cybermaze.features.game.BaseLevelViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class Level02ViewModel @Inject constructor(
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

        require(LevelLoader.validateLayout(LEVEL_02_MAP).isEmpty()) {
            "Level 02 layout invalid: ${LevelLoader.validateLayout(LEVEL_02_MAP)}"
        }

        val map = LevelLoader.loadFromStringArray(LEVEL_02_MAP, LEVEL_02_CONFIG)
        setupLevel(
            LevelDefinition(
                config = LEVEL_02_CONFIG,
                map = map,
                enemies = LEVEL_02_ENEMIES.map { it.copy() },
                extraSystems = listOf(PowerUpSystem),
                palette = LEVEL_02_PALETTE
            )
        )
    }

    override fun createInitialState(): GameState? {
        val current = gameState.value ?: return null
        val map = LevelLoader.loadFromStringArray(LEVEL_02_MAP, LEVEL_02_CONFIG)
        val collectibles = collectibleSystem.createCollectiblesFromMap(map)
        return current.copy(
            player = Player(position = map.playerSpawn),
            enemies = LEVEL_02_ENEMIES.map { it.copy() },
            map = map,
            config = LEVEL_02_CONFIG,
            collectibles = collectibles,
            collectedPoints = 0,
            elapsedTime = 0f,
            livesLost = 0,
            traps = emptyList(),
            teleports = emptyList(),
            powerUpSecondsLeft = 0f,
            fogVisionBoostSecondsLeft = 0f,
            fogBaseRadiusTiles = null,
            phase = GamePhase.BRIEFING,
            palette = LEVEL_02_PALETTE
        )
    }
}
