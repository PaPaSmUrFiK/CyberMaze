package com.cybermaze.features.level_02

import com.cybermaze.core.game.model.LevelConfig

/** Energy tiles `o` on [LEVEL_02_MAP] (two replaced by P on row 1). */
const val LEVEL_02_TOTAL_POINTS: Int = 47

/** ~81% to clear. */
const val LEVEL_02_REQUIRED_POINTS: Int = 38

val LEVEL_02_CONFIG: LevelConfig = LevelConfig(
    levelNumber = 2,
    title = "Data Harvest",
    description = "Collect power-ups to slow hostile drones. Plan routes around patrols.",
    timeLimit = null,
    requiredPoints = LEVEL_02_REQUIRED_POINTS,
    totalPoints = LEVEL_02_TOTAL_POINTS,
    hasKeys = false,
    hasTeleports = false,
    hasDarkness = false,
    hasMovingTraps = false,
    specialMechanic = "Power-up slows all enemies for 5s"
)
