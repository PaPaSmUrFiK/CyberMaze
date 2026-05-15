package com.cybermaze.features.level_07

import com.cybermaze.core.game.model.Position
import com.cybermaze.core.game.model.TeleportPair

/** Warp between mid-maze pads `A` / `B` on row 7 ([LEVEL_07_MAP]). */
val LEVEL_07_TELEPORTS: List<TeleportPair> = listOf(
    TeleportPair(
        id = "meadow_warp",
        posA = Position(7, 7),
        posB = Position(21, 7),
        cooldownDuration = 1.2f
    )
)
