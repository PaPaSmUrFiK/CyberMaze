package com.cybermaze.features.level_09

import com.cybermaze.core.game.level.LevelLoader
import com.cybermaze.core.game.model.LevelConfig

/**
 * Win: reach exit with enough energy. [requiredPoints] ≈ 80% of all `o` tiles
 * (same rule of thumb as Level 1 / Level 5).
 */
val LEVEL_09_CONFIG: LevelConfig by lazy {
    val total = LevelLoader.countTiles(LEVEL_09_MAP, 'o')
    val required = (total * 8 + 9) / 10
    LevelConfig(
        levelNumber = 9,
        title = "Deep Maze",
        description = "Reach and use A↔B to cross the split maze. Enemies enter 10s frenzy every 30s.",
        timeLimit = 180,
        requiredPoints = required,
        totalPoints = total,
        hasKeys = true,
        hasTeleports = true,
        hasDarkness = false,
        hasMovingTraps = false,
        specialMechanic = "Mandatory teleport crossing + 30s/10s enemy frenzy cycle + one shield pickup."
    )
}
