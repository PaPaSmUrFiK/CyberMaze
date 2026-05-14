package com.cybermaze.features.level_08

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
 * Long patrols on rows 3 and 7 (disjoint); chasers / fast off those rows; guard watches up-ramp toward exit lane.
 */
val LEVEL_08_ENEMIES: List<Enemy> = listOf(
    Enemy(
        id = "chaser_01",
        position = Position(7, 4),
        type = EnemyType.CHASER,
        speed = 0.8f,
        direction = Direction.RIGHT
    ),
    Enemy(
        id = "chaser_02",
        position = Position(19, 4),
        type = EnemyType.CHASER,
        speed = 1.0f,
        direction = Direction.LEFT
    ),
    Enemy(
        id = "patrol_01",
        position = Position(1, 3),
        type = EnemyType.PATROL,
        speed = 0.7f,
        patrolPath = pingPongHorizontal(y = 3, xMin = 1, xMax = 25),
        patrolIndex = 0
    ),
    Enemy(
        id = "patrol_02",
        position = Position(1, 7),
        type = EnemyType.PATROL,
        speed = 0.7f,
        patrolPath = pingPongHorizontal(y = 7, xMin = 1, xMax = 25),
        patrolIndex = 0
    ),
    Enemy(
        id = "fast_01",
        position = Position(13, 9),
        type = EnemyType.FAST,
        speed = 1.6f
    ),
    Enemy(
        id = "guard_01",
        position = Position(24, 9),
        type = EnemyType.GUARD,
        lookDirection = Direction.UP,
        speed = 0f
    )
)
