package com.cybermaze.features.level_template

import com.cybermaze.core.game.model.MovingTrap
import com.cybermaze.core.game.model.Position

/**
 * Example [MovingTrap] — a diamond hazard that steps along [MovingTrap.path] every
 * [MovingTrap.moveInterval] seconds.
 *
 * The first [MovingTrap.position] should match [MovingTrap.path.first] at level start
 * (see [com.cybermaze.features.game.BaseLevelViewModel] `createInitialState` trap reset).
 */
val LEVEL_TEMPLATE_TRAPS: List<MovingTrap> = listOf(
    MovingTrap(
        id = "trap_row",
        position = Position(12, 5),
        path = listOf(
            Position(12, 5),
            Position(20, 5)
        ),
        moveInterval = 0.85f
    )
)
