package com.cybermaze.features.level_template

import com.cybermaze.core.game.level.LevelLoader
import com.cybermaze.core.game.model.LevelConfig

/**
 * Example [LevelConfig] for the template level.
 *
 * When you copy this package to `level_02` … `level_10`, change:
 * - [LevelConfig.levelNumber] / [LevelConfig.title] / [LevelConfig.description]
 * - [requiredPoints] / [totalPoints] — must match how many `o` tiles you placed
 *   (use [LevelLoader.countTiles] on your map string array).
 * - Feature flags (`hasKeys`, `hasTeleports`, …) — used by UI / docs; core logic
 *   is driven by which [com.cybermaze.core.game.engine.LevelSystem]s you attach.
 */
val LEVEL_TEMPLATE_CONFIG: LevelConfig by lazy {
    LevelConfig(
        levelNumber = 99,
        title = "Template Sandbox",
        description = "Donor package — not registered in NavGraph. Copy to build a real level.",
        timeLimit = null,
        requiredPoints = 4,
        totalPoints = LevelLoader.countTiles(LEVEL_TEMPLATE_MAP, 'o'),
        hasKeys = true,
        hasTeleports = true,
        hasDarkness = true,
        hasMovingTraps = true,
        specialMechanic = "Demonstrates all LevelSystems + overlay layers in one place."
    )
}
