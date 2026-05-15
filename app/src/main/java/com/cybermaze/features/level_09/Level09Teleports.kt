package com.cybermaze.features.level_09

import com.cybermaze.core.game.model.Position
import com.cybermaze.core.game.model.TeleportPair

/** Single mandatory A ↔ B bridge between map halves. */
val LEVEL_09_TELEPORTS: List<TeleportPair> = listOf(
    TeleportPair(
        id = "deep_warp",
        posA = Position(5, 5),   // 'A' tile col 5, row 5
        posB = Position(25, 5),  // 'B' tile col 25, row 5
        cooldownDuration = 1.5f
    )
)
