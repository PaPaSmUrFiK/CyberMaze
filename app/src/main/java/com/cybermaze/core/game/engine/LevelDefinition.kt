package com.cybermaze.core.game.engine

import com.cybermaze.core.game.model.Enemy
import com.cybermaze.core.game.model.GameMap
import com.cybermaze.core.game.model.LevelConfig
import com.cybermaze.core.game.model.MovingTrap
import com.cybermaze.core.game.model.TeleportPair
import com.cybermaze.core.ui.theme.LevelPalette

/**
 * Bootstrap payload for a single level: static layout + pluggable systems.
 */
data class LevelDefinition(
    val config: LevelConfig,
    val map: GameMap,
    val enemies: List<Enemy>,
    val traps: List<MovingTrap> = emptyList(),
    val teleports: List<TeleportPair> = emptyList(),
    /** When non-null, fog-of-war uses this base radius in tiles (see FogOfWarSystem). */
    val baseFogRadiusTiles: Int? = null,
    val extraSystems: List<LevelSystem> = emptyList(),
    /** Per-level color palette; omit for the default Cyber Maze look. */
    val palette: LevelPalette = LevelPalette.Default
)
