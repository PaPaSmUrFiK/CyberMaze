package com.cybermaze.features.level_10

import com.cybermaze.core.game.model.MovingTrap
import com.cybermaze.core.game.model.Position

private fun horizontalPingPong(y: Int, xMin: Int, xMax: Int): List<Position> =
    (xMin..xMax).map { Position(it, y) } +
        (xMax - 1 downTo xMin).map { Position(it, y) }

/**
 * Five moving traps across upper, mid, and lower zones; waypoints are walkable in [LEVEL_10_MAP].
 */
val LEVEL_10_TRAPS: List<MovingTrap> = listOf(
    MovingTrap(
        id = "trap_upper",
        position = Position(20, 1),
        path = horizontalPingPong(y = 1, xMin = 20, xMax = 26),
        moveInterval = 0.88f
    ),
    MovingTrap(
        id = "trap_row3",
        position = Position(23, 3),
        path = listOf(
            Position(23, 3),
            Position(26, 3)
        ),
        moveInterval = 0.85f
    ),
    MovingTrap(
        id = "trap_row5",
        position = Position(8, 5),
        path = listOf(
            Position(8, 5),
            Position(11, 5)
        ),
        moveInterval = 0.75f
    ),
    MovingTrap(
        id = "trap_row7",
        position = Position(17, 7),
        path = listOf(
            Position(17, 7),
            Position(19, 7)
        ),
        moveInterval = 0.90f
    ),
    MovingTrap(
        id = "trap_vertical",
        position = Position(13, 5),
        path = listOf(
            Position(13, 5),
            Position(13, 6),
            Position(13, 7),
            Position(13, 8),
            Position(13, 9),
            Position(13, 8),
            Position(13, 7),
            Position(13, 6)
        ),
        moveInterval = 0.65f
    )
)
