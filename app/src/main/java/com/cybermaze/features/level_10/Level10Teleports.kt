package com.cybermaze.features.level_10

import com.cybermaze.core.game.model.Position
import com.cybermaze.core.game.model.TeleportPair

/**
 * Single long-range teleport pair on row 3 of [LEVEL_10_MAP].
 *
 * Positions must exactly match `A` / `B` tile coordinates so the
 * [com.cybermaze.core.game.system.TeleportSystem] can fire on landing.
 */
val LEVEL_10_TELEPORTS: List<TeleportPair> = listOf(
    TeleportPair(
        id = "core_warp",
        posA = Position(6, 3),
        posB = Position(30, 3),
        cooldownDuration = 1.5f
    )
)
