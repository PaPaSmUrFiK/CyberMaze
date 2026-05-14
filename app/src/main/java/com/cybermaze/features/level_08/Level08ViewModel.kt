package com.cybermaze.features.level_08

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
import com.cybermaze.features.game.BaseLevelViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class Level08ViewModel @Inject constructor(
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

        require(LevelLoader.validateLayout(LEVEL_08_MAP).isEmpty()) {
            "Level 08 layout invalid: ${LevelLoader.validateLayout(LEVEL_08_MAP)}"
        }

        val cfg = LEVEL_08_CONFIG
        val map = LevelLoader.loadFromStringArray(LEVEL_08_MAP, cfg)
        setupLevel(
            LevelDefinition(
                config = cfg,
                map = map,
                enemies = LEVEL_08_ENEMIES.map { it.copy() },
                extraSystems = listOf(GuardSystem(collisionSystem)),
                palette = LEVEL_08_PALETTE
            )
        )
    }

    override fun createInitialState(): GameState? {
        val current = gameState.value ?: return null
        val cfg = LEVEL_08_CONFIG
        val map = LevelLoader.loadFromStringArray(LEVEL_08_MAP, cfg)
        val collectibles = collectibleSystem.createCollectiblesFromMap(map)
        return current.copy(
            player = Player(position = map.playerSpawn),
            enemies = LEVEL_08_ENEMIES.map { it.copy() },
            map = map,
            config = cfg,
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
            palette = LEVEL_08_PALETTE
        )
    }
}
