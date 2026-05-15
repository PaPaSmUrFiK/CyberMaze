package com.cybermaze.features.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cybermaze.core.data.repository.ProgressRepository
import com.cybermaze.core.game.engine.GameEvent
import com.cybermaze.core.game.engine.GameEventEmitter
import com.cybermaze.core.game.engine.GameLoop
import com.cybermaze.core.game.engine.HazardDamage
import com.cybermaze.core.game.engine.LevelDefinition
import com.cybermaze.core.game.engine.LevelSystem
import com.cybermaze.core.game.level.LevelResult
import com.cybermaze.core.game.model.CollectibleType
import com.cybermaze.core.game.model.Direction
import com.cybermaze.core.game.model.Enemy
import com.cybermaze.core.game.model.GameMap
import com.cybermaze.core.game.model.GamePhase
import com.cybermaze.core.game.model.GameState
import com.cybermaze.core.game.model.LevelConfig
import com.cybermaze.core.game.model.Player
import com.cybermaze.core.game.model.TileType
import com.cybermaze.core.game.system.CollectibleSystem
import com.cybermaze.core.game.system.CollisionSystem
import com.cybermaze.core.game.system.EnemySystem
import com.cybermaze.core.game.system.MovementSystem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Base ViewModel for every level. Subclasses provide a [LevelDefinition] (preferred)
 * or call the legacy [setupLevel] overload with map + config + enemies only.
 *
 * Pluggable [LevelSystem]s in [LevelDefinition.extraSystems] run after core physics
 * each tick and receive collectible / landing / hit hooks.
 */
abstract class BaseLevelViewModel(
    protected val progressRepository: ProgressRepository,
    protected val movementSystem: MovementSystem,
    protected val collisionSystem: CollisionSystem,
    protected val enemySystem: EnemySystem,
    protected val collectibleSystem: CollectibleSystem
) : ViewModel() {

    private val _gameState = MutableStateFlow<GameState?>(null)
    val gameState: StateFlow<GameState?> = _gameState.asStateFlow()

    private val _events = MutableSharedFlow<GameEvent>(extraBufferCapacity = 32)
    val events: SharedFlow<GameEvent> = _events.asSharedFlow()

    private val gameLoop = GameLoop(onUpdate = ::onTick)

    private var extraSystems: List<LevelSystem> = emptyList()

    private var initialLives: Int = 3
    private var totalEnergyPoints: Int = 0
    private var progressSaved: Boolean = false

    private val emit: GameEventEmitter = { _events.tryEmit(it) }

    // ---------------------------------------------------------------------
    // Lifecycle
    // ---------------------------------------------------------------------

    /**
     * Preferred entry point: pass map, traps, teleports, fog radius, and any
     * [LevelSystem] implementations (see package `com.cybermaze.core.game.system`).
     */
    protected fun setupLevel(definition: LevelDefinition) {
        extraSystems = definition.extraSystems
        val player = Player(position = definition.map.playerSpawn)
        initialLives = player.lives
        val collectibles = collectibleSystem.createCollectiblesFromMap(definition.map)
        totalEnergyPoints = collectibles.count { it.type == CollectibleType.ENERGY_POINT }
        progressSaved = false
        _gameState.value = GameState(
            player = player,
            enemies = definition.enemies,
            map = definition.map,
            config = definition.config,
            collectibles = collectibles,
            traps = definition.traps,
            teleports = definition.teleports,
            fogBaseRadiusTiles = definition.baseFogRadiusTiles,
            palette = definition.palette,
            phase = GamePhase.BRIEFING
        )
        gameLoop.launchIn(viewModelScope)
    }

    /** Dismiss the pre-game briefing and start the simulation. */
    fun onStart() {
        val state = _gameState.value ?: return
        if (state.phase == GamePhase.BRIEFING) {
            _gameState.value = state.updatePhase(GamePhase.PLAYING)
            gameLoop.resume()
        }
    }

    /** Legacy overload — builds a [LevelDefinition] with no extras. */
    protected fun setupLevel(map: GameMap, config: LevelConfig, enemies: List<Enemy>) {
        setupLevel(
            LevelDefinition(
                config = config,
                map = map,
                enemies = enemies
            )
        )
    }

    override fun onCleared() {
        super.onCleared()
        gameLoop.stop()
    }

    // ---------------------------------------------------------------------
    // Public API
    // ---------------------------------------------------------------------

    /**
     * Sets the player's intended direction only. Actual steps run in [onTick] when
     * [Player.canStep] is true — so swipes cannot bypass [Player.moveTimer] and spam tiles.
     */
    fun onDirectionInput(direction: Direction) {
        if (direction == Direction.NONE) return
        val state = _gameState.value ?: return
        if (state.phase != GamePhase.PLAYING) return
        _gameState.value = state.updatePlayer(state.player.withDirection(direction))
    }

    fun onPause() {
        val state = _gameState.value ?: return
        if (state.phase == GamePhase.PLAYING) {
            gameLoop.pause()
            _gameState.value = state.updatePhase(GamePhase.PAUSED)
            emit(GameEvent.GamePaused)
        }
    }

    fun onResume() {
        val state = _gameState.value ?: return
        if (state.phase == GamePhase.PAUSED) {
            _gameState.value = state.updatePhase(GamePhase.PLAYING)
            gameLoop.resume()
            emit(GameEvent.GameResumed)
        }
    }

    open fun onRestart() {
        val fresh = createInitialState() ?: return
        progressSaved = false
        initialLives = fresh.player.lives
        totalEnergyPoints = fresh.collectibles.count { it.type == CollectibleType.ENERGY_POINT }
        _gameState.value = fresh
        if (gameLoop.isRunning().not()) {
            gameLoop.launchIn(viewModelScope)
        } else {
            gameLoop.resume()
        }
    }

    /**
     * Subclasses that use [LevelDefinition] with non-empty [LevelDefinition.extraSystems]
     * should override [onRestart] and/or [createInitialState] to re-assign the same
     * [extraSystems] list (e.g. rebuild from `LevelXXViewModel` fields).
     *
     * Default implementation resets the current map's collectibles and core counters.
     */
    protected open fun createInitialState(): GameState? {
        val state = _gameState.value ?: return null
        val collectibles = collectibleSystem.createCollectiblesFromMap(state.map)
        val resetTraps = state.traps.map { trap ->
            val start = trap.path.firstOrNull() ?: trap.position
            trap.copy(position = start, pathIndex = 0, moveTimer = 0f)
        }
        val resetTeleports = state.teleports.map { it.copy(cooldownA = 0f, cooldownB = 0f) }
        return state.copy(
            player = Player(position = state.map.playerSpawn),
            enemies = state.enemies.map { e ->
                val atStart = if (e.patrolPath.isNotEmpty()) e.patrolPath[0] else e.position
                e.copy(
                    speedMultiplier = 1f,
                    moveTimer = 0f,
                    rotationTimer = 0f,
                    patrolIndex = if (e.patrolPath.isNotEmpty()) 0 else e.patrolIndex,
                    position = atStart
                )
            },
            collectibles = collectibles,
            collectedPoints = 0,
            elapsedTime = 0f,
            livesLost = 0,
            phase = GamePhase.PLAYING,
            traps = resetTraps,
            teleports = resetTeleports,
            powerUpSecondsLeft = 0f,
            fogVisionBoostSecondsLeft = 0f
        )
    }

    // ---------------------------------------------------------------------
    // Simulation tick
    // ---------------------------------------------------------------------

    private fun onTick(deltaTime: Float) {
        val state = _gameState.value ?: return
        if (state.phase != GamePhase.PLAYING) return

        var next = state.updateTime(deltaTime)
        val tickedPlayer = next.player.advanceTimers(deltaTime)
        next = next.updatePlayer(tickedPlayer)

        if (tickedPlayer.direction != Direction.NONE && tickedPlayer.canStep()) {
            val moved = movementSystem.movePlayer(tickedPlayer, tickedPlayer.direction, next.map)
            val playerAfter = if (moved.position != tickedPlayer.position) {
                moved.resetMoveTimer()
            } else {
                // Blocked — snap prev to current so the renderer doesn't slide,
                // and clamp the timer so input picks up immediately when freed.
                tickedPlayer.copy(
                    previousPosition = tickedPlayer.position,
                    moveTimer = tickedPlayer.effectiveStepInterval()
                )
            }
            next = next.updatePlayer(playerAfter)
            if (playerAfter.position != tickedPlayer.position) {
                next = handlePlayerLanding(next)
                emit(GameEvent.PlayerMoved(playerAfter.position))
            }
        }

        val updatedEnemies = enemySystem.updateEnemies(
            enemies = next.enemies,
            playerPos = next.player.position,
            map = next.map,
            deltaTime = deltaTime
        )
        next = next.updateEnemies(updatedEnemies)

        if (collisionSystem.checkPlayerEnemyCollision(next.player, next.enemies)) {
            next = applyEnemyHit(next)
        }

        next = extraSystems.fold(next) { s, sys -> sys.update(s, deltaTime, emit) }

        next = checkEndConditions(next)

        _gameState.value = next
    }

    private fun handlePlayerLanding(state: GameState): GameState {
        var current = state

        val pickups = collisionSystem.checkPlayerCollectible(current.player, current.collectibles)
        pickups.forEach { item ->
            val (newPlayer, newCollectibles) = collectibleSystem.collectItem(
                player = current.player,
                collectible = item,
                collectibles = current.collectibles
            )
            current = current.updatePlayer(newPlayer).updateCollectibles(newCollectibles)
            if (item.type == CollectibleType.ENERGY_POINT) {
                current = current.collectPoint()
            }
            emit(GameEvent.CollectibleCollected(item.type, item.position))
            current = extraSystems.fold(current) { s, sys ->
                sys.onCollectibleCollected(s, item.type, item.position, emit)
            }
        }

        val tileType = collisionSystem.checkPlayerTile(current.player, current.map)
        if (tileType == TileType.DOOR &&
            collisionSystem.canOpenDoor(current.player, current.player.position, current.map)
        ) {
            val (newPlayer, newMap) = collisionSystem.openDoor(
                player = current.player,
                doorPosition = current.player.position,
                map = current.map
            )
            current = current.updatePlayer(newPlayer).updateMap(newMap)
            emit(GameEvent.DoorOpened(newPlayer.position))
        }

        current = extraSystems.fold(current) { s, sys -> sys.onPlayerLanded(s, emit) }
        return current
    }

    private fun applyEnemyHit(state: GameState): GameState {
        if (state.player.isInvincible) return state
        val beforeLives = state.player.lives
        var next = HazardDamage.applyPlayerHit(state, collisionSystem)
        emit(GameEvent.PlayerHit(next.player.lives))
        if (next.player.lives < beforeLives) {
            if (next.player.lives <= 0) {
                emit(GameEvent.PlayerDied)
            } else {
                emit(GameEvent.PlayerRespawned)
            }
        }
        next = extraSystems.fold(next) { s, sys -> sys.onPlayerHit(s, emit) }
        return next
    }

    private fun checkEndConditions(state: GameState): GameState {
        return when {
            state.hasWon() -> {
                val final = state.updatePhase(GamePhase.WIN)
                onLevelCompleted(final)
                final
            }
            state.hasLost() -> {
                val final = state.updatePhase(GamePhase.LOSE)
                onLevelFailed(final)
                final
            }
            else -> state
        }
    }

    private fun onLevelCompleted(state: GameState) {
        if (progressSaved) return
        progressSaved = true
        gameLoop.pause()
        val allCollected = state.collectedPoints >= totalEnergyPoints && totalEnergyPoints > 0
        val noLivesLost = state.livesLost == 0
        val stars = LevelResult.calculateStars(
            completed = true,
            timeSeconds = state.elapsedTime,
            timeLimit = state.config.timeLimit,
            allPointsCollected = allCollected,
            noLivesLost = noLivesLost
        )
        emit(GameEvent.LevelCompleted(state.player.score, stars))
        viewModelScope.launch {
            progressRepository.completeLevel(
                levelNumber = state.config.levelNumber,
                score = state.player.score,
                timeSeconds = state.elapsedTime,
                stars = stars
            )
        }
    }

    private fun onLevelFailed(state: GameState) {
        gameLoop.pause()
        val reason = if (!state.player.isAlive()) "no_lives" else "time"
        emit(GameEvent.LevelFailed(reason))
    }

    fun calculateStars(state: GameState): Int {
        val allCollected = state.collectedPoints >= totalEnergyPoints && totalEnergyPoints > 0
        return LevelResult.calculateStars(
            completed = state.phase == GamePhase.WIN,
            timeSeconds = state.elapsedTime,
            timeLimit = state.config.timeLimit,
            allPointsCollected = allCollected,
            noLivesLost = state.livesLost == 0
        )
    }

    fun totalEnergy(): Int = totalEnergyPoints

    /** Re-attach pluggable systems after [createInitialState] (subclasses with extras call this). */
    protected fun setExtraSystems(systems: List<LevelSystem>) {
        extraSystems = systems
    }
}
