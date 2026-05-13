package com.cybermaze.features.level_10

import com.cybermaze.core.game.model.MovingTrap
import com.cybermaze.core.game.model.Position

/**
 * Four moving traps for Level 10 — three horizontal patrols on different rows
 * plus one vertical patrol that crosses the lower half of the map.
 *
 * Every trap waypoint must sit on a walkable tile in [LEVEL_10_MAP].
 */
val LEVEL_10_TRAPS: List<MovingTrap> = listOf(
    MovingTrap(
        id = "trap_row3",
        position = Position(10, 3),
        path = listOf(
            Position(10, 3),
            Position(14, 3)
        ),
        moveInterval = 0.85f
    ),
    MovingTrap(
        id = "trap_row5",
        position = Position(20, 5),
        path = listOf(
            Position(20, 5),
            Position(24, 5)
        ),
        moveInterval = 0.75f
    ),
    MovingTrap(
        id = "trap_row7",
        position = Position(8, 7),
        path = listOf(
            Position(8, 7),
            Position(12, 7)
        ),
        moveInterval = 0.90f
    ),
    MovingTrap(
        id = "trap_vertical",
        position = Position(16, 5),
        path = listOf(
            Position(16, 5),
            Position(16, 7),
            Position(16, 9),
            Position(16, 11),
            Position(16, 9),
            Position(16, 7)
        ),
        moveInterval = 0.65f
    )
)
