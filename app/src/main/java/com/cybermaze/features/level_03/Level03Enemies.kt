package com.cybermaze.features.level_03

import com.cybermaze.core.game.model.Enemy
import com.cybermaze.core.game.model.EnemyType
import com.cybermaze.core.game.model.Position

val LEVEL_03_ENEMIES: List<Enemy> = listOf(

    Enemy(
        id = "fast_01",
        position = Position(10, 3),
        type = EnemyType.FAST,
        speed = 1.5f
    ),

    Enemy(
        id = "fast_02",
        position = Position(18, 5),
        type = EnemyType.FAST,
        speed = 1.5f
    ),

    Enemy(
        id = "fast_03",
        position = Position(22, 8),
        type = EnemyType.FAST,
        speed = 1.8f
    )
)