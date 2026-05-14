package com.cybermaze.features.level_03

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
import com.cybermaze.core.game.system.SpeedBoostSystem
import com.cybermaze.features.game.BaseLevelViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class Level03ViewModel @Inject constructor(
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

        viewModelScope.launch {
            progressRepository.initializeProgress()
        }

        require(LevelLoader.validateLayout(LEVEL_03_MAP).isEmpty()) {
            "Level layout invalid: ${LevelLoader.validateLayout(LEVEL_03_MAP)}"
        }

        val cfg = LEVEL_03_CONFIG

        val map = LevelLoader.loadFromStringArray(
            LEVEL_03_MAP,
            cfg
        )

        val systems = listOf(
            SpeedBoostSystem
        )

        setupLevel(
            LevelDefinition(
                config = cfg,
                map = map,
                enemies = LEVEL_03_ENEMIES.map { it.copy() },
                extraSystems = systems,
                palette = LEVEL_03_PALETTE
            )
        )
    }

    override fun createInitialState(): GameState? {

        val current = gameState.value ?: return null

        val cfg = LEVEL_03_CONFIG

        val map = LevelLoader.loadFromStringArray(
            LEVEL_03_MAP,
            cfg
        )

        val collectibles = collectibleSystem.createCollectiblesFromMap(map)

        return current.copy(
            player = Player(position = map.playerSpawn),

            enemies = LEVEL_03_ENEMIES.map { enemy ->
                enemy.copy(
                    speedMultiplier = 1f,
                    moveTimer = 0f,
                    rotationTimer = 0f
                )
            },

            map = map,
            config = cfg,

            collectibles = collectibles,

            collectedPoints = 0,
            elapsedTime = 0f,
            livesLost = 0,

            phase = GamePhase.PLAYING
        )
    }

    override fun onRestart() {

        setExtraSystems(
            listOf(
                SpeedBoostSystem
            )
        )

        super.onRestart()
    }
}