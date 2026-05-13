package com.cybermaze.features.level_template

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
 * Reference ViewModel: wires **every** optional [com.cybermaze.core.game.engine.LevelSystem].
 *
 * **Not** registered in [com.cybermaze.core.navigation.NavGraph] — copy this package and
 * delete systems / layers you do not need for your real level.
 */
@HiltViewModel
class LevelTemplateViewModel @Inject constructor(
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

        require(LevelLoader.validateLayout(LEVEL_TEMPLATE_MAP).isEmpty()) {
            "Template layout invalid: ${LevelLoader.validateLayout(LEVEL_TEMPLATE_MAP)}"
        }

        // `LEVEL_TEMPLATE_CONFIG` is `by lazy` so `LEVEL_TEMPLATE_MAP` is initialized first.
        val cfg = LEVEL_TEMPLATE_CONFIG
        val map = LevelLoader.loadFromStringArray(LEVEL_TEMPLATE_MAP, cfg)

        // Order matters when multiple systems listen to the same collectible type
        // (e.g. POWER_UP: enemy slow + fog vision — keep PowerUp before Fog).
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
                enemies = LEVEL_TEMPLATE_ENEMIES.map { it.copy() },
                traps = LEVEL_TEMPLATE_TRAPS.map { it.copy() },
                teleports = LEVEL_TEMPLATE_TELEPORTS.map { it.copy() },
                baseFogRadiusTiles = 3,
                extraSystems = systems,
                // Delete this line — or set `palette = LevelPalette.Default` — to use the
                // default Cyber Maze look on your real level.
                palette = LEVEL_TEMPLATE_PALETTE
            )
        )
    }

    override fun createInitialState(): GameState? {
        val current = gameState.value ?: return null
        val cfg = LEVEL_TEMPLATE_CONFIG
        val map = LevelLoader.loadFromStringArray(LEVEL_TEMPLATE_MAP, cfg)
        val collectibles = collectibleSystem.createCollectiblesFromMap(map)
        val resetTraps = LEVEL_TEMPLATE_TRAPS.map { t ->
            val start = t.path.firstOrNull() ?: t.position
            t.copy(position = start, pathIndex = 0, moveTimer = 0f)
        }
        return current.copy(
            player = Player(position = map.playerSpawn),
            enemies = LEVEL_TEMPLATE_ENEMIES.map { it.copy() },
            map = map,
            config = cfg,
            collectibles = collectibles,
            collectedPoints = 0,
            elapsedTime = 0f,
            livesLost = 0,
            phase = GamePhase.PLAYING,
            traps = resetTraps,
            teleports = LEVEL_TEMPLATE_TELEPORTS.map { it.copy(cooldownA = 0f, cooldownB = 0f) },
            powerUpSecondsLeft = 0f,
            fogVisionBoostSecondsLeft = 0f,
            fogBaseRadiusTiles = 3,
            palette = LEVEL_TEMPLATE_PALETTE
        )
    }
}
