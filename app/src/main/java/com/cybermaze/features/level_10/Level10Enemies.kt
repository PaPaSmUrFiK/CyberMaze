package com.cybermaze.features.level_10

import com.cybermaze.core.game.model.Direction
import com.cybermaze.core.game.model.Enemy
import com.cybermaze.core.game.model.EnemyType
import com.cybermaze.core.game.model.Position

private fun pingPongHorizontal(y: Int, xMin: Int, xMax: Int): List<Position> {
    val forward = (xMin..xMax).map { Position(it, y) }
    val backward = (xMax - 1 downTo xMin).map { Position(it, y) }
    return forward + backward
}

/**
 * Twelve enemies spread across upper, mid, and lower zones — not clustered on row 11 only.
 */
val LEVEL_10_ENEMIES: List<Enemy> = listOf(
    Enemy(
        id = "patrol_upper",
        position = Position(8, 1),
        type = EnemyType.PATROL,
        speed = 0.78f,
        patrolPath = pingPongHorizontal(y = 1, xMin = 8, xMax = 28),
        patrolIndex = 0
    ),
    Enemy(
        id = "chaser_upper",
        position = Position(22, 3),
        type = EnemyType.CHASER,
        speed = 0.95f,
        direction = Direction.LEFT
    ),
    Enemy(
        id = "random_upper",
        position = Position(12, 3),
        type = EnemyType.RANDOM,
        speed = 0.68f
    ),
    Enemy(
        id = "fast_mid",
        position = Position(8, 5),
        type = EnemyType.FAST,
        speed = 1.55f
    ),
    Enemy(
        id = "chaser_mid",
        position = Position(6, 5),
        type = EnemyType.CHASER,
        speed = 1.0f,
        direction = Direction.RIGHT
    ),
    Enemy(
        id = "guard_mid_door",
        position = Position(12, 7),
        type = EnemyType.GUARD,
        lookDirection = Direction.RIGHT,
        speed = 0f
    ),
    Enemy(
        id = "random_mid",
        position = Position(4, 7),
        type = EnemyType.RANDOM,
        speed = 0.7f
    ),
    Enemy(
        id = "fast_lower",
        position = Position(4, 9),
        type = EnemyType.FAST,
        speed = 1.5f
    ),
    Enemy(
        id = "chaser_lower",
        position = Position(20, 9),
        type = EnemyType.CHASER,
        speed = 0.9f,
        direction = Direction.DOWN
    ),
    Enemy(
        id = "patrol_lower",
        position = Position(14, 11),
        type = EnemyType.PATROL,
        speed = 0.75f,
        patrolPath = pingPongHorizontal(y = 11, xMin = 14, xMax = 24),
        patrolIndex = 0
    ),
    Enemy(
        id = "guard_sentry",
        position = Position(26, 5),
        type = EnemyType.GUARD,
        lookDirection = Direction.LEFT,
        speed = 0f
    ),
    Enemy(
        id = "guard_exit_lane",
        position = Position(22, 11),
        type = EnemyType.GUARD,
        lookDirection = Direction.LEFT,
        speed = 0f
    )
)
