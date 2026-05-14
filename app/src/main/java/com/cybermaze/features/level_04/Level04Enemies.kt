package com.cybermaze.features.level_04

import com.cybermaze.core.game.model.Enemy
import com.cybermaze.core.game.model.EnemyType
import com.cybermaze.core.game.model.Position

/**
 * Enemy definitions for Level 04.
 */
val LEVEL_04_ENEMIES = listOf(
    Enemy(
        id = "patrol_01",
        position = Position(3, 5),
        type = EnemyType.PATROL,
        speedMultiplier = 0.8f,
        patrolPath = listOf(
            Position(1, 5),
            Position(2, 5),
            Position(3, 5),
            Position(4, 5),
            Position(5, 5),
            Position(4, 5),
            Position(3, 5),
            Position(2, 5)
        )
    ),
    Enemy(
        id = "patrol_02",
        position = Position(11, 4),
        type = EnemyType.PATROL,
        speedMultiplier = 0.7f,
        patrolPath = listOf(
            Position(10, 4),
            Position(11, 4),
            Position(12, 4),
            Position(13, 4),
            Position(12, 4),
            Position(11, 4)
        )
    ),
    Enemy(
        id = "random_01",
        position = Position(20, 6),
        type = EnemyType.RANDOM,
        speedMultiplier = 0.6f
    )
)
