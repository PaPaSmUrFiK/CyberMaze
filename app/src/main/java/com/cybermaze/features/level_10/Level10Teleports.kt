package com.cybermaze.features.level_10

import com.cybermaze.core.game.model.Position
import com.cybermaze.core.game.model.TeleportPair

/**
 * Two warp pairs on [LEVEL_10_MAP]: mid maze (row 5) and long top/bottom jump (rows 1 / 11).
 */
val LEVEL_10_TELEPORTS: List<TeleportPair> = listOf(
    TeleportPair(
        id = "core_warp",
        posA = Position(14, 5),
        posB = Position(18, 5),
        cooldownDuration = 1.5f
    ),
    TeleportPair(
        id = "spire_warp",
        posA = Position(29, 1),
        posB = Position(30, 11),
        cooldownDuration = 2.0f
    )
)
