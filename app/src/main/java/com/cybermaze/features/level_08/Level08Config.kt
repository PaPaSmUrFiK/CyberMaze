package com.cybermaze.features.level_08

import com.cybermaze.core.game.model.LevelConfig

const val LEVEL_08_TOTAL_POINTS: Int = 47

const val LEVEL_08_REQUIRED_POINTS: Int = 38

val LEVEL_08_CONFIG: LevelConfig = LevelConfig(
    levelNumber = 8,
    title = "Hunter Pack",
    description = "Shields absorb one hit. Watch the guard's cone near the exit.",
    timeLimit = null,
    requiredPoints = LEVEL_08_REQUIRED_POINTS,
    totalPoints = LEVEL_08_TOTAL_POINTS,
    hasKeys = false,
    hasTeleports = false,
    hasDarkness = false,
    hasMovingTraps = false,
    specialMechanic = "Guard vision + shield pickups"
)
