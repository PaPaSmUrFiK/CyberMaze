package com.cybermaze.core.game.model

import com.cybermaze.core.ui.theme.LevelPalette

/**
 * Complete game state at a moment in time.
 *
 * Immutable: each system / handler returns a copy with the relevant changes.
 *
 * [livesLost] tracks how many lives the player has spent in this run, which
 * is used to award the perfect-run star independent of the current life count.
 */
data class GameState(
    val player: Player,
    val enemies: List<Enemy>,
    val map: GameMap,
    val config: LevelConfig,
    val collectibles: List<Collectible> = emptyList(),
    val collectedPoints: Int = 0,
    val elapsedTime: Float = 0f,
    val phase: GamePhase = GamePhase.PLAYING,
    val livesLost: Int = 0,
    val traps: List<MovingTrap> = emptyList(),
    val teleports: List<TeleportPair> = emptyList(),
    /** Global slow-enemy power-up countdown (seconds). */
    val powerUpSecondsLeft: Float = 0f,
    /** Base fog radius in tiles; null = fog disabled for this level. */
    val fogBaseRadiusTiles: Int? = null,
    /** Extra vision radius tiles while fog power-up is active (TZ: +2 to reach 5 from 3). */
    val fogVisionBoostSecondsLeft: Float = 0f,
    /** Per-level visual palette. Renderers read colors from here, not from [com.cybermaze.core.ui.theme.CyberColors]. */
    val palette: LevelPalette = LevelPalette.Default
) {
    val score: Int get() = player.score

    /** Player reached the exit with enough collectibles. */
    fun hasWon(): Boolean =
        config.hasEnoughPoints(collectedPoints) && player.position == map.exitPosition

    /** Player ran out of lives, or timer expired on a timed level. */
    fun hasLost(): Boolean =
        !player.isAlive() ||
            (config.isTimed() && elapsedTime >= (config.timeLimit ?: Int.MAX_VALUE))

    fun updatePlayer(newPlayer: Player): GameState = copy(player = newPlayer)
    fun updateEnemies(newEnemies: List<Enemy>): GameState = copy(enemies = newEnemies)
    fun updateCollectibles(newCollectibles: List<Collectible>): GameState =
        copy(collectibles = newCollectibles)
    fun updateMap(newMap: GameMap): GameState = copy(map = newMap)
    fun updatePhase(newPhase: GamePhase): GameState = copy(phase = newPhase)
    fun updateTime(deltaTime: Float): GameState = copy(elapsedTime = elapsedTime + deltaTime)
    fun collectPoint(points: Int = 1): GameState = copy(collectedPoints = collectedPoints + points)
    fun recordLifeLost(): GameState = copy(livesLost = livesLost + 1)
    fun updateTraps(newTraps: List<MovingTrap>): GameState = copy(traps = newTraps)
    fun updateTeleports(newTeleports: List<TeleportPair>): GameState = copy(teleports = newTeleports)

    /** Total energy points on this map (for the HUD). */
    val totalEnergy: Int
        get() = collectibles.count { it.type == CollectibleType.ENERGY_POINT }

    /** Remaining seconds (only for timed levels). */
    fun remainingTime(): Int? = config.timeLimit?.let { limit ->
        (limit - elapsedTime.toInt()).coerceAtLeast(0)
    }

    /** Effective fog radius for rendering / visibility queries. */
    fun effectiveFogRadiusTiles(): Int? {
        val base = fogBaseRadiusTiles ?: return null
        return if (fogVisionBoostSecondsLeft > 0f) base + 2 else base
    }
}
