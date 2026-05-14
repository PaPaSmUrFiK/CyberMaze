package com.cybermaze.features.level_06

import com.cybermaze.core.game.level.LevelLoader
import com.cybermaze.core.game.model.LevelConfig

/** 120-second timer per TZ §6.6. */
const val LEVEL_06_TIME_LIMIT: Int = 120

/** Level becomes winnable once the player collects at least this many points. */
const val LEVEL_06_REQUIRED_POINTS: Int = 20

val LEVEL_06_CONFIG: LevelConfig by lazy {
    LevelConfig(
        levelNumber = 6,
        title = "Warp Zone",
        description = "Navigate the grid using teleportation pads. Watch the cooldown!",
        timeLimit = LEVEL_06_TIME_LIMIT,
        requiredPoints = LEVEL_06_REQUIRED_POINTS,
        totalPoints = LevelLoader.countTiles(LEVEL_06_MAP, 'o'),
        hasTeleports = true,
        specialMechanic = "Two pairs of bidirectional teleports (A and B)."
    )
}
