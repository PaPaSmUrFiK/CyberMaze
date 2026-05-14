package com.cybermaze.features.level_02

import com.cybermaze.core.game.model.Enemy
import com.cybermaze.core.game.model.EnemyType
import com.cybermaze.core.game.model.Position

private fun pingPongHorizontal(y: Int, xMin: Int, xMax: Int): List<Position> {
    val forward = (xMin..xMax).map { Position(it, y) }
    val backward = (xMax - 1 downTo xMin).map { Position(it, y) }
    return forward + backward
}

/**
 * Two long row patrols (y = 3 and y = 7) never share tiles; random roams mid maze.
 * Speeds: patrols 0.7, random 0.6 (TZ).
 */
val LEVEL_02_ENEMIES: List<Enemy> = listOf(
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
        id = "random_01",
        position = Position(13, 5),
        type = EnemyType.RANDOM,
        speed = 0.6f
    )
)
