package com.cybermaze.features.level_03

import com.cybermaze.core.game.level.LevelLoader
import com.cybermaze.core.game.model.LevelConfig

val LEVEL_03_CONFIG: LevelConfig by lazy {
    LevelConfig(
        levelNumber = 3,
        title = "Speed Protocol",
        description = "Reach the exit before the timer expires.",
        timeLimit = 90,
        requiredPoints = LevelLoader.countTiles(LEVEL_03_MAP, 'o'),
        totalPoints = LevelLoader.countTiles(LEVEL_03_MAP, 'o'),
        hasKeys = false,
        hasTeleports = false,
        hasDarkness = false,
        hasMovingTraps = false,
        specialMechanic = "Speed Boost + Countdown Timer"
    )
}