package com.cybermaze.features.level_07

import com.cybermaze.core.game.level.LevelLoader
import com.cybermaze.core.game.model.LevelConfig

private val level07EnergyTotal: Int = LevelLoader.countTiles(LEVEL_07_MAP, 'o')

const val LEVEL_07_REQUIRED_POINTS: Int = 18

val LEVEL_07_CONFIG: LevelConfig = LevelConfig(
    levelNumber = 7,
    title = "Unicorn Meadow",
    description = "Pastel maze — no fog. Find keys in the center and a deep wing; one door guards the exit run. Warp pads, speed (S) and slow-field (P) help.",
    timeLimit = null,
    requiredPoints = LEVEL_07_REQUIRED_POINTS,
    totalPoints = level07EnergyTotal,
    hasKeys = true,
    hasTeleports = true,
    hasDarkness = false,
    hasMovingTraps = true,
    specialMechanic = "Keys/doors, teleports, S/P pickups; guard vision + moving traps (no fog)."
)
