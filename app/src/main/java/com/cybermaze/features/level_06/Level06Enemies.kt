package com.cybermaze.features.level_06

import com.cybermaze.core.game.model.Enemy
import com.cybermaze.core.game.model.EnemyType
import com.cybermaze.core.game.model.Position

/**
 * 4 enemies for Level 06 per TZ §6.6.
 */
val LEVEL_06_ENEMIES = listOf(
    // Chaser in the middle area
    Enemy(
        id = "chaser_01",
        position = Position(14, 5),
        type = EnemyType.CHASER,
        speed = 0.8f
    ),
    // Patrol on the left side
    Enemy(
        id = "patrol_01",
        position = Position(1, 5),
        type = EnemyType.PATROL,
        speed = 0.7f,
        patrolPath = listOf(
            Position(1, 5), Position(2, 5), Position(3, 5), Position(4, 5),
            Position(4, 5), Position(3, 5), Position(2, 5), Position(1, 5)
        )
    ),
    // Patrol on the right side
    Enemy(
        id = "patrol_02",
        position = Position(27, 5),
        type = EnemyType.PATROL,
        speed = 0.7f,
        patrolPath = listOf(
            Position(27, 5), Position(26, 5), Position(25, 5), Position(24, 5),
            Position(24, 5), Position(25, 5), Position(26, 5), Position(27, 5)
        )
    ),
    // Fast random enemy near the exit
    Enemy(
        id = "fast_01",
        position = Position(20, 9),
        type = EnemyType.FAST,
        speed = 1.2f
    )
)
