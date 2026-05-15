package com.cybermaze.features.level_07

import com.cybermaze.core.game.model.Direction
import com.cybermaze.core.game.model.Enemy
import com.cybermaze.core.game.model.EnemyType
import com.cybermaze.core.game.model.Position

private fun pingPongHorizontal(y: Int, xMin: Int, xMax: Int): List<Position> {
    val forward = (xMin..xMax).map { Position(it, y) }
    val backward = (xMax - 1 downTo xMin).map { Position(it, y) }
    return forward + backward
}

/** Seven enemies — lighter than before; one guard watches the exit choke door. */
val LEVEL_07_ENEMIES: List<Enemy> = listOf(
    Enemy(
        id = "chaser_01",
        position = Position(3, 3),
        type = EnemyType.CHASER,
        speed = 0.82f,
        direction = Direction.RIGHT
    ),
    Enemy(
        id = "chaser_02",
        position = Position(22, 3),
        type = EnemyType.CHASER,
        speed = 0.85f,
        direction = Direction.LEFT
    ),
    Enemy(
        id = "patrol_top",
        position = Position(4, 1),
        type = EnemyType.PATROL,
        speed = 0.62f,
        patrolPath = pingPongHorizontal(y = 1, xMin = 4, xMax = 22),
        patrolIndex = 0
    ),
    Enemy(
        id = "patrol_bottom",
        position = Position(5, 11),
        type = EnemyType.PATROL,
        speed = 0.58f,
        patrolPath = pingPongHorizontal(y = 11, xMin = 5, xMax = 20),
        patrolIndex = 0
    ),
    Enemy(
        id = "fast_01",
        position = Position(9, 9),
        type = EnemyType.FAST,
        speed = 1.25f
    ),
    Enemy(
        id = "random_01",
        position = Position(6, 5),
        type = EnemyType.RANDOM,
        speed = 0.55f
    ),
    Enemy(
        id = "guard_exit_door",
        position = Position(11, 11),
        type = EnemyType.GUARD,
        lookDirection = Direction.RIGHT,
        speed = 0f
    )
)
