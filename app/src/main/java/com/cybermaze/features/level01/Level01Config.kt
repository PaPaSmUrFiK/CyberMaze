package com.cybermaze.features.level01

import com.cybermaze.core.game.model.LevelConfig

/**
 * Tutorial-level configuration ("Boot Sector").
 *
 * Numbers below come straight from [LEVEL_01_MAP] — change them together if
 * the layout changes:
 *   - 49 energy points on the map.
 *   - 80% pickup rate is required for the level to count as won.
 */
const val LEVEL_01_TOTAL_POINTS: Int = 49
const val LEVEL_01_REQUIRED_POINTS: Int = 40

val LEVEL_01_CONFIG: LevelConfig = LevelConfig(
    levelNumber = 1,
    title = "Boot Sector",
    description = "Initialize your systems, Agent.",
    timeLimit = null,
    requiredPoints = LEVEL_01_REQUIRED_POINTS,
    totalPoints = LEVEL_01_TOTAL_POINTS,
    hasKeys = false,
    hasTeleports = false,
    hasDarkness = false,
    hasMovingTraps = false,
    specialMechanic = null
)
