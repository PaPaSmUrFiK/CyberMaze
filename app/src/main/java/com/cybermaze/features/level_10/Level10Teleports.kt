package com.cybermaze.features.level_10

import com.cybermaze.core.game.model.Position
import com.cybermaze.core.game.model.TeleportPair

/**
 * Teleport pair — coordinates match `A` / `B` glyphs in [LEVEL_10_MAP] (row 5).
 */
val LEVEL_10_TELEPORTS: List<TeleportPair> = listOf(
    TeleportPair(
        id = "core_warp",
        posA = Position(14, 5),
        posB = Position(18, 5),
        cooldownDuration = 1.5f
    )
)
