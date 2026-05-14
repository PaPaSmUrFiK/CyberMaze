package com.cybermaze.features.level_10

import com.cybermaze.core.game.model.Direction
import com.cybermaze.core.game.model.Enemy
import com.cybermaze.core.game.model.EnemyType
import com.cybermaze.core.game.model.Position

/**
 * Ten enemies for Level 10 — CHASER / PATROL / FAST / RANDOM doubled, **two** [EnemyType.GUARD]
 * (vision cone via [com.cybermaze.core.ui.renderer.GuardVisionLayer]). Positions match walkable
 * tiles in [LEVEL_10_MAP].
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
        id = "patrol_lower",
        position = Position(12, 11),
        type = EnemyType.PATROL,
        patrolPath = listOf(
            Position(12, 11),
            Position(20, 11)
        )
    ),
    Enemy(
        id = "patrol_upper",
        position = Position(17, 7),
        type = EnemyType.PATROL,
        patrolPath = listOf(
            Position(17, 7),
            Position(19, 7)
        )
    ),
    Enemy(
        id = "fast_left",
        position = Position(4, 9),
        type = EnemyType.FAST
    ),
    Enemy(
        id = "fast_right",
        position = Position(30, 9),
        type = EnemyType.FAST
    ),
    Enemy(
        id = "random_top",
        position = Position(24, 3),
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
    ),
    Enemy(
        id = "guard_sentry",
        position = Position(26, 5),
        type = EnemyType.GUARD,
        lookDirection = Direction.LEFT,
        speed = 0f
    )
)
