package com.cybermaze.features.level_template

import com.cybermaze.core.game.model.Position
import com.cybermaze.core.game.model.TeleportPair

/**
 * Example [TeleportPair] — positions must align with `A` / `B` tiles in [LEVEL_TEMPLATE_MAP].
 */
val LEVEL_TEMPLATE_TELEPORTS: List<TeleportPair> = listOf(
    TeleportPair(
        id = "pair_main",
        posA = Position(12, 3),
        posB = Position(16, 3),
        cooldownDuration = 1.5f
    )
)
