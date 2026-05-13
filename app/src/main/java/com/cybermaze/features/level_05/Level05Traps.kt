package com.cybermaze.features.level_05

import com.cybermaze.core.game.model.MovingTrap
import com.cybermaze.core.game.model.Position

/**
 * Three moving traps for Level 05 — "Trap Matrix".
 *
 * Per TZ §6.5:
 *   trap_01: horizontal patrol at y=3
 *   trap_02: vertical patrol at x=7
 *   trap_03: perimeter of inner square
 */
val LEVEL_05_TRAPS: List<MovingTrap> = listOf(
    // Trap 01: horizontal along row 4 (y=4 in 0-index = TZ's y=3)
    MovingTrap(
        id = "trap_horizontal",
        position = Position(5, 4),
        path = listOf(
            Position(5, 4),
            Position(10, 4),
            Position(15, 4),
            Position(20, 4),
            Position(15, 4),
            Position(10, 4)
        ),
        moveInterval = 0.6f
    ),

    // Trap 02: vertical along column 8 (x=8 in 0-index = TZ's x=7)
    MovingTrap(
        id = "trap_vertical",
        position = Position(8, 3),
        path = listOf(
            Position(8, 3),
            Position(8, 5),
            Position(8, 7),
            Position(8, 5)
        ),
        moveInterval = 1.0f
    ),

    // Trap 03: perimeter of inner rectangle (approx 12,3 to 18,7)
    MovingTrap(
        id = "trap_perimeter",
        position = Position(12, 3),
        path = listOf(
            Position(12, 3),  // top-left
            Position(15, 3),  // top-mid
            Position(18, 3),  // top-right
            Position(18, 5),  // right-mid
            Position(18, 7),  // bottom-right
            Position(15, 7),  // bottom-mid
            Position(12, 7),  // bottom-left
            Position(12, 5),  // left-mid
            Position(12, 3)   // back to start
        ),
        moveInterval = 1.2f
    )
)