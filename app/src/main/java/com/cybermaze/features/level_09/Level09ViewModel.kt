package com.cybermaze.features.level_09

import androidx.lifecycle.viewModelScope
import com.cybermaze.core.data.repository.ProgressRepository
import com.cybermaze.core.game.engine.LevelDefinition
import com.cybermaze.core.game.level.LevelLoader
import com.cybermaze.core.game.model.GamePhase
import com.cybermaze.core.game.model.GameState
import com.cybermaze.core.game.model.Player
import com.cybermaze.core.game.model.Position
import com.cybermaze.core.game.system.CollectibleSystem
import com.cybermaze.core.game.system.CollisionSystem
import com.cybermaze.core.game.system.EnemySystem
import com.cybermaze.core.game.system.MovementSystem
import com.cybermaze.core.game.system.TeleportSystem
import com.cybermaze.features.game.BaseLevelViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/** ViewModel for Level 9 — "Deep Maze". */
@HiltViewModel
class Level09ViewModel @Inject constructor(
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
    private val frenzySystem = Level09FrenzySystem()
    // HazardPortalSystem needs frenzySystem reference to detect first frenzy.
    private val hazardPortalSystem = Level09HazardPortalSystem(frenzySystem)
    private val afkSystem = Level09AfkSystem(collisionSystem)

    init {
        viewModelScope.launch { progressRepository.initializeProgress() }

        require(LevelLoader.validateLayout(LEVEL_09_MAP).isEmpty()) {
            "Level 09 layout is invalid: ${LevelLoader.validateLayout(LEVEL_09_MAP)}"
        }

        val cfg = LEVEL_09_CONFIG
        val map = LevelLoader.loadFromStringArray(LEVEL_09_MAP, cfg)

        setupLevel(
            LevelDefinition(
                config = cfg,
                map = map,
                enemies = LEVEL_09_ENEMIES.map { it.copy() },
                traps = emptyList(),
                teleports = LEVEL_09_TELEPORTS.map { it.copy() },
                baseFogRadiusTiles = null,
                extraSystems = listOf(
                    frenzySystem,
                    hazardPortalSystem,   // reads frenzySystem.isFrenzyActive() each tick
                    TeleportSystem,
                    afkSystem             // runs last — reads final position after teleport
                ),
                palette = LEVEL_09_PALETTE_NORMAL
            )
        )
    }

    override fun createInitialState(): GameState? {
        val current = gameState.value ?: return null
        val cfg = LEVEL_09_CONFIG
        val map = LevelLoader.loadFromStringArray(LEVEL_09_MAP, cfg)
        val collectibles = collectibleSystem.createCollectiblesFromMap(map)
        return current.copy(
            player = Player(position = map.playerSpawn),
            enemies = LEVEL_09_ENEMIES.map { it.copy() },
            map = map,
            config = cfg,
            collectibles = collectibles,
            collectedPoints = 0,
            elapsedTime = 0f,
            livesLost = 0,
            phase = GamePhase.BRIEFING,
            traps = emptyList(),
            teleports = LEVEL_09_TELEPORTS.map { it.copy(cooldownA = 0f, cooldownB = 0f) },
            powerUpSecondsLeft = 0f,
            fogVisionBoostSecondsLeft = 0f,
            fogBaseRadiusTiles = null,
            palette = LEVEL_09_PALETTE_NORMAL
        )
    }

    override fun onRestart() {
        frenzySystem.reset()
        hazardPortalSystem.reset()
        afkSystem.reset()
        super.onRestart()
    }

    // ── Exposed to Level09Screen ────────────────────────────────────────────
    fun isFrenzyActive(): Boolean = frenzySystem.isFrenzyActive()
    fun frenzyPulse01(): Float = frenzySystem.frenzyPulse01()
    fun secondsToFrenzyModeSwitch(): Int = frenzySystem.secondsToNextMode()
    fun isPortalHazardActive(): Boolean = hazardPortalSystem.isHazardActive()
    fun isAfkWarning(): Boolean = afkSystem.isWarning
    fun warpDecoyPosition(): Position? = hazardPortalSystem.decoyWarpPosition()
    fun isWarpPreviewActive(): Boolean = hazardPortalSystem.isWarpPreviewActive()
}
