package com.cybermaze.features.level01

import com.cybermaze.core.game.model.Enemy
import com.cybermaze.core.game.model.EnemyType
import com.cybermaze.core.game.model.Position

/**
 * Enemies for Level 01 ("Boot Sector").
 *
 * One slow patrol that runs up and down the central column (x = 13).
 * Every position on the patrol path is on a walkable tile in [LEVEL_01_MAP].
 *
 * Speed 0.6 = one step every ~1.66 s — easy to learn the swerve mechanics.
 */
val LEVEL_01_ENEMIES: List<Enemy> = listOf(
    Enemy(
        id = "patrol_01",
        position = Position(13, 3),
        type = EnemyType.PATROL,
        speed = 0.6f,
        patrolPath = listOf(
            Position(13, 3),
            Position(13, 4),
            Position(13, 5),
            Position(13, 6),
            Position(13, 7),
            Position(13, 8),
            Position(13, 9),
            Position(13, 8),
            Position(13, 7),
            Position(13, 6),
            Position(13, 5),
            Position(13, 4)
        ),
        patrolIndex = 0
    )
)
