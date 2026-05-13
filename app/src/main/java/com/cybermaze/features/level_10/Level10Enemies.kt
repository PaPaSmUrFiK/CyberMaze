package com.cybermaze.features.level_10

import com.cybermaze.core.game.model.Direction
import com.cybermaze.core.game.model.Enemy
import com.cybermaze.core.game.model.EnemyType
import com.cybermaze.core.game.model.Position

/**
 * Nine enemies for Level 10 — one of every [EnemyType] x2 (plus a single GUARD).
 *
 * Per TZ §6.10 the final core has "9 enemies of all types". All positions sit
 * on walkable `.` / `o` tiles in [LEVEL_10_MAP].
 */
val LEVEL_10_ENEMIES: List<Enemy> = listOf(
    Enemy(
        id = "chaser_left",
        position = Position(3, 11),
        type = EnemyType.CHASER,
        direction = Direction.RIGHT
    ),
    Enemy(
        id = "chaser_right",
        position = Position(31, 11),
        type = EnemyType.CHASER,
        direction = Direction.LEFT
    ),
    Enemy(
        id = "patrol_mid",
        position = Position(8, 9),
        type = EnemyType.PATROL,
        patrolPath = listOf(
            Position(8, 9),
            Position(15, 9)
        )
    ),
    Enemy(
        id = "patrol_upper",
        position = Position(5, 7),
        type = EnemyType.PATROL,
        patrolPath = listOf(
            Position(5, 7),
            Position(15, 7)
        )
    ),
    Enemy(
        id = "fast_left",
        position = Position(3, 9),
        type = EnemyType.FAST
    ),
    Enemy(
        id = "fast_right",
        position = Position(33, 9),
        type = EnemyType.FAST
    ),
    Enemy(
        id = "random_top",
        position = Position(20, 7),
        type = EnemyType.RANDOM
    ),
    Enemy(
        id = "random_bottom",
        position = Position(10, 11),
        type = EnemyType.RANDOM
    ),
    Enemy(
        id = "guard_core",
        position = Position(18, 11),
        type = EnemyType.GUARD,
        lookDirection = Direction.UP,
        speed = 0f
    )
)
