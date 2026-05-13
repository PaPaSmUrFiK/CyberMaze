package com.cybermaze.features.level_05

import com.cybermaze.core.game.model.Enemy
import com.cybermaze.core.game.model.EnemyType
import com.cybermaze.core.game.model.Position

/**
 * Three enemies for Level 05 per TZ §6.5.
 *
 * Note: TZ says "3 врага" but does NOT specify types.
 * Using basic patrol/chaser types — traps are the main hazard.
 */
// Level05Enemies.kt — 3 врага, но быстрее
val LEVEL_05_ENEMIES: List<Enemy> = listOf(
    Enemy(
        id = "chaser_fast",
        position = Position(8, 5),
        type = EnemyType.CHASER,
        speed = 1.0f  // быстрее
    ),
    Enemy(
        id = "patrol_vertical",
        position = Position(18, 3),
        type = EnemyType.PATROL,
        speed = 0.9f,  // быстрее
        patrolPath = listOf(
            Position(18, 3), Position(18, 5),
            Position(18, 7), Position(18, 9),  // длиннее
            Position(18, 7), Position(18, 5)
        )
    ),
    Enemy(
        id = "fast_random",
        position = Position(12, 7),
        type = EnemyType.FAST,  // FAST вместо CHASER
        speed = 1.3f
    )
)