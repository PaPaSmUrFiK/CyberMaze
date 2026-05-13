package com.cybermaze.features.level_05

import com.cybermaze.core.game.model.LevelConfig

/** Count energy points in LEVEL_05_MAP = 29 */
const val LEVEL_05_TOTAL_POINTS: Int = 29

/** Need ~80% to win (23 точек из 29) */
const val LEVEL_05_REQUIRED_POINTS: Int = 23

val LEVEL_05_CONFIG: LevelConfig = LevelConfig(
    levelNumber = 5,
    title = "Trap Matrix",
    description = "Moving traps patrol the corridors. Avoid the red pulses!",
    timeLimit = null,
    requiredPoints = LEVEL_05_REQUIRED_POINTS,
    totalPoints = LEVEL_05_TOTAL_POINTS,
    hasKeys = false,
    hasTeleports = false,
    hasDarkness = false,
    hasMovingTraps = true,
    specialMechanic = "Moving traps follow set paths"
)