package com.cybermaze.features.level_06

import com.cybermaze.core.game.model.Position
import com.cybermaze.core.game.model.TeleportPair

/**
 * Teleport pairs for Level 06.
 * Pair A: (7, 1) <-> (21, 9)
 * Pair B: (22, 1) <-> (6, 9)
 * (Coordinates calculated from LEVEL_06_MAP layout)
 */
val LEVEL_06_TELEPORTS = listOf(
    TeleportPair(
        id = "warp_a",
        posA = Position(7, 1),
        posB = Position(21, 9)
    ),
    TeleportPair(
        id = "warp_b",
        posA = Position(22, 1),
        posB = Position(6, 9)
    )
)
