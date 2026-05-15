package com.cybermaze.features.level_07

import com.cybermaze.core.game.model.MovingTrap
import com.cybermaze.core.game.model.Position

/** Full back-and-forth path (one tile per step), same pattern as [com.cybermaze.features.level_05]. */
private fun horizontalPingPong(y: Int, xMin: Int, xMax: Int): List<Position> =
    (xMin..xMax).map { Position(it, y) } +
        (xMax - 1 downTo xMin).map { Position(it, y) }

/** Two horizontal moving traps on long walkable rows (see [LEVEL_07_MAP]). */
val LEVEL_07_TRAPS: List<MovingTrap> = listOf(
    MovingTrap(
        id = "trap_top_corridor",
        position = Position(12, 1),
        path = horizontalPingPong(y = 1, xMin = 12, xMax = 22),
        moveInterval = 0.78f
    ),
    MovingTrap(
        id = "trap_bottom_corridor",
        position = Position(12, 11),
        path = horizontalPingPong(y = 11, xMin = 12, xMax = 20),
        moveInterval = 0.74f
    )
)
