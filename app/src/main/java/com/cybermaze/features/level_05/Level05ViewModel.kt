package com.cybermaze.features.level_05

import androidx.lifecycle.viewModelScope
import com.cybermaze.core.data.repository.ProgressRepository
import com.cybermaze.core.game.engine.LevelDefinition
import com.cybermaze.core.game.level.LevelLoader
import com.cybermaze.core.game.model.GamePhase
import com.cybermaze.core.game.model.GameState
import com.cybermaze.core.game.model.MovingTrap
import com.cybermaze.core.game.model.Player
import com.cybermaze.core.game.system.CollectibleSystem
import com.cybermaze.core.game.system.CollisionSystem
import com.cybermaze.core.game.system.EnemySystem
import com.cybermaze.core.game.system.MovementSystem
import com.cybermaze.core.game.system.MovingTrapSystem
import com.cybermaze.features.game.BaseLevelViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Level 05 — "Trap Matrix".
 *
 * Features: 3 moving traps + 3 enemies + temporary trap disable button.
 * Button becomes available after collecting 10 energy points.
 */
@HiltViewModel
class Level05ViewModel @Inject constructor(
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

    private var originalTraps: List<MovingTrap> = emptyList()
    private val _areTrapsDisabled = MutableStateFlow(false)
    val areTrapsDisabled = _areTrapsDisabled.asStateFlow()

    // Флаг: доступна ли кнопка обезвреживания
    private val _isDisarmButtonAvailable = MutableStateFlow(false)
    val isDisarmButtonAvailable = _isDisarmButtonAvailable.asStateFlow()

    private var trapsDisabledJob: kotlinx.coroutines.Job? = null

    init {
        viewModelScope.launch { progressRepository.initializeProgress() }

        require(LevelLoader.validateLayout(LEVEL_05_MAP).isEmpty()) {
            "Level 05 layout invalid: ${LevelLoader.validateLayout(LEVEL_05_MAP)}"
        }

        val cfg = LEVEL_05_CONFIG
        val map = LevelLoader.loadFromStringArray(LEVEL_05_MAP, cfg)
        originalTraps = LEVEL_05_TRAPS.map { it.copy() }

        val systems = listOf(
            MovingTrapSystem(collisionSystem)
        )

        setupLevel(
            LevelDefinition(
                config = cfg,
                map = map,
                enemies = LEVEL_05_ENEMIES.map { it.copy() },
                traps = originalTraps.map { it.copy() },
                teleports = emptyList(),
                baseFogRadiusTiles = null,
                extraSystems = systems,
                palette = LEVEL_05_PALETTE
            )
        )
    }

    /**
     * Проверяет, можно ли активировать кнопку (после сбора 10 точек)
     * Вызывается при каждом обновлении состояния
     */
    fun checkDisarmButtonAvailability() {
        val currentState = gameState.value ?: return
        val shouldBeAvailable = currentState.collectedPoints >= 10
        if (shouldBeAvailable != _isDisarmButtonAvailable.value) {
            _isDisarmButtonAvailable.value = shouldBeAvailable
        }
    }

    /**
     * Временно отключает ловушки на 4 секунды
     */
    fun disableTrapsTemporarily() {
        val currentState = gameState.value ?: return
        if (currentState.phase != GamePhase.PLAYING) return
        if (trapsDisabledJob?.isActive == true) return
        if (_areTrapsDisabled.value) return
        if (!_isDisarmButtonAvailable.value) return  // Кнопка недоступна!

        _areTrapsDisabled.value = true

        trapsDisabledJob = viewModelScope.launch {
            delay(4000)
            _areTrapsDisabled.value = false
            trapsDisabledJob = null
        }
    }

    fun getVisibleTraps(currentTraps: List<MovingTrap>): List<MovingTrap> {
        return if (_areTrapsDisabled.value) emptyList() else currentTraps
    }

    override fun createInitialState(): GameState? {
        val current = gameState.value ?: return null
        val cfg = LEVEL_05_CONFIG
        val map = LevelLoader.loadFromStringArray(LEVEL_05_MAP, cfg)
        val collectibles = collectibleSystem.createCollectiblesFromMap(map)

        val resetTraps = originalTraps.map { t ->
            val start = t.path.firstOrNull() ?: t.position
            t.copy(position = start, pathIndex = 0, moveTimer = 0f)
        }

        trapsDisabledJob?.cancel()
        trapsDisabledJob = null
        _areTrapsDisabled.value = false
        _isDisarmButtonAvailable.value = false  // Сброс доступности кнопки

        return current.copy(
            player = Player(position = map.playerSpawn),
            enemies = LEVEL_05_ENEMIES.map { it.copy() },
            map = map,
            config = cfg,
            collectibles = collectibles,
            collectedPoints = 0,
            elapsedTime = 0f,
            livesLost = 0,
            phase = GamePhase.BRIEFING,
            traps = resetTraps,
            teleports = emptyList(),
            powerUpSecondsLeft = 0f,
            fogVisionBoostSecondsLeft = 0f,
            fogBaseRadiusTiles = null,
            palette = LEVEL_05_PALETTE
        )
    }

    override fun onRestart() {
        trapsDisabledJob?.cancel()
        trapsDisabledJob = null
        _areTrapsDisabled.value = false
        _isDisarmButtonAvailable.value = false
        super.onRestart()
    }
}