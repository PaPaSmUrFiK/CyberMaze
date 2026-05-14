package com.cybermaze.features.level_10

import com.cybermaze.core.game.level.LevelLoader
import com.cybermaze.core.game.model.LevelConfig

/** 180-second hard timer per TZ §6.10. */
const val LEVEL_10_TIME_LIMIT: Int = 180

/**
 * Counted from [LEVEL_10_MAP] — keep in sync if you edit the layout.
 * Level becomes winnable once the player collects at least [LEVEL_10_REQUIRED_POINTS].
 */
const val LEVEL_10_REQUIRED_POINTS: Int = 32

val LEVEL_10_CONFIG: LevelConfig by lazy {
    LevelConfig(
        levelNumber = 10,
        title = "Final Core",
        description = "Timer, traps, teleports, two guards (vision), 3 keys / 3 chokepoint doors, full visibility.",
        timeLimit = LEVEL_10_TIME_LIMIT,
        requiredPoints = LEVEL_10_REQUIRED_POINTS,
        totalPoints = LevelLoader.countTiles(LEVEL_10_MAP, 'o'),
        hasKeys = true,
        hasTeleports = true,
        hasDarkness = false,
        hasMovingTraps = true,
        specialMechanic = "Boss: 10 enemies (2 guards w/ vision), traps, teleports, 3 chokepoint doors."
    )
}
