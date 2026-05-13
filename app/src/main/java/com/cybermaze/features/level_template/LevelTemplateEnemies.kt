package com.cybermaze.features.level_template

import com.cybermaze.core.game.model.Direction
import com.cybermaze.core.game.model.Enemy
import com.cybermaze.core.game.model.EnemyType
import com.cybermaze.core.game.model.Position

/**
 * One example of each [EnemyType] so you can delete what you do not need.
 *
 * Positions must sit on walkable `.` tiles in [LEVEL_TEMPLATE_MAP] (adjust if you edit the map).
 */
val LEVEL_TEMPLATE_ENEMIES: List<Enemy> = listOf(
    Enemy(
        id = "patrol_1",
        position = Position(5, 5),
        type = EnemyType.PATROL,
        patrolPath = listOf(
            Position(5, 5),
            Position(9, 5),
            Position(9, 7),
            Position(5, 7)
        )
    ),
    Enemy(
        id = "chaser_1",
        position = Position(20, 5),
        type = EnemyType.CHASER,
        direction = Direction.LEFT
    ),
    Enemy(
        id = "random_1",
        position = Position(15, 7),
        type = EnemyType.RANDOM
    ),
    Enemy(
        id = "fast_1",
        position = Position(25, 7),
        type = EnemyType.FAST
    ),
    Enemy(
        id = "guard_1",
        position = Position(28, 3),
        type = EnemyType.GUARD,
        lookDirection = Direction.LEFT,
        speed = 0f
    )
)
