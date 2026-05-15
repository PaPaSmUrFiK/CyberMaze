package com.cybermaze.features.level_10

import com.cybermaze.core.game.level.LevelLoader
import com.cybermaze.core.game.model.LevelConfig

/** 180-second hard timer per TZ §6.10. */
const val LEVEL_10_TIME_LIMIT: Int = 180

/**
 * Counted from [LEVEL_10_MAP] — keep in sync if you edit the layout.
 * Level becomes winnable once the player collects at least [LEVEL_10_REQUIRED_POINTS].
 */
val LEVEL_10_CONFIG: LevelConfig by lazy {
    val totalEnergy = LevelLoader.countTiles(LEVEL_10_MAP, 'o')
    LevelConfig(
        levelNumber = 10,
        title = "Final Core",
        description = "Boss run: keys hide in dead ends, shield tucked upstairs, warps and S/P pickups. Guards only hit in the tile ahead of them.",
        timeLimit = LEVEL_10_TIME_LIMIT,
        requiredPoints = (totalEnergy * 0.88f).toInt().coerceAtLeast(1),
        totalPoints = totalEnergy,
        hasKeys = true,
        hasTeleports = true,
        hasDarkness = false,
        hasMovingTraps = true,
        specialMechanic = "Boss: 12 enemies zone-wide, 5 traps, 2 warps, hidden keys/H/S/P, 3 doors; guard melee range 1."
    )
}

/** ~88% of energy on the map — keep in sync when editing [LEVEL_10_MAP]. */
val LEVEL_10_REQUIRED_POINTS: Int
    get() = LEVEL_10_CONFIG.requiredPoints
