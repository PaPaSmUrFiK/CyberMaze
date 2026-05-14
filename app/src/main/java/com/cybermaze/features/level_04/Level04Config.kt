package com.cybermaze.features.level_04

import com.cybermaze.core.game.model.LevelConfig

/**
 * Level 04 configuration ("Locked Grid").
 *
 * This level introduces keys and doors. The player must collect keys (K)
 * to pass through doors (D).
 */
const val LEVEL_04_TOTAL_POINTS: Int = 22
const val LEVEL_04_REQUIRED_POINTS: Int = 22

val LEVEL_04_CONFIG: LevelConfig = LevelConfig(
    levelNumber = 4,
    title = "Locked Grid",
    description = "Collect keys to decrypt the firewall doors.",
    timeLimit = null,
    requiredPoints = LEVEL_04_REQUIRED_POINTS,
    totalPoints = LEVEL_04_TOTAL_POINTS,
    hasKeys = true,
    hasTeleports = false,
    hasDarkness = false,
    hasMovingTraps = false,
    specialMechanic = "Keys and Doors"
)
