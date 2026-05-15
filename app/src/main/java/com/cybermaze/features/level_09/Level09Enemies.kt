package com.cybermaze.features.level_09

import com.cybermaze.core.game.model.Direction
import com.cybermaze.core.game.model.Enemy
import com.cybermaze.core.game.model.EnemyType
import com.cybermaze.core.game.model.Position

/**
 * Level 9 enemies (7 total: 3 chasers, 2 patrols, 2 random).
 *
 * chaser_starter  — 7 tiles right of spawn (8,1), pursues player immediately.
 * chaser_portal   — PATROL near portal A (longer path). Warp: ring preview + decoy hold, then CHASER;
 *                   destination alternates bottom/top between runs. Becomes CHASER after warp.
 * chaser_01       — top-right corridor, guards exit approach.
 * patrol_01       — left section, extended path covers rows 7–9 near K1.
 * patrol_03       — bottom-right horizontal patrol (row 11); path avoids walls.
 * random_01/02    — right half; become CHASER-behaviour during frenzy.
 *
 * Removed: chaser_02 (bottom-left), patrol_02 (yellow left-centre), guard_01 (purple right).
 */
val LEVEL_09_ENEMIES: List<Enemy> = listOf(

    // ── Instant-start chaser ────────────────────────────────────────────────
    Enemy(
        id = "chaser_starter",
        position = Position(8, 1),
        type = EnemyType.CHASER,
        direction = Direction.LEFT,
        speed = 0.8f
    ),

    // ── Portal guardian ─────────────────────────────────────────────────────
    // PATROL near portal A; path extends two tiles down column 5 then returns.
    // Red tint drawn in [Level09WarpVfx]; warp preview + teleport in [Level09HazardPortalSystem].
    Enemy(
        id = "chaser_portal",
        position = Position(7, 5),
        type = EnemyType.PATROL,
        direction = Direction.LEFT,
        speed = 0.8f,
        patrolPath = listOf(
            Position(7, 5),
            Position(6, 5),
            Position(5, 5),
            Position(5, 6),
            Position(5, 7),
            Position(5, 6),
            Position(5, 5),
            Position(6, 5)
        )
    ),

    // ── Top-right chaser ────────────────────────────────────────────────────
    Enemy(
        id = "chaser_01",
        position = Position(28, 1),
        type = EnemyType.CHASER,
        direction = Direction.LEFT,
        speed = 0.9f
    ),

    // ── Patrols — extended paths, all steps to adjacent passable tiles ───────
    //
    // patrol_01: row 9 (col 3–6) extended up through col 3, rows 9→8→7.
    // 12-step cycle — 2× original length.
    Enemy(
        id = "patrol_01",
        position = Position(3, 9),
        type = EnemyType.PATROL,
        patrolPath = listOf(
            Position(3, 9), Position(4, 9), Position(5, 9), Position(6, 9),
            Position(5, 9), Position(4, 9), Position(3, 9),
            Position(3, 8), Position(3, 7), Position(3, 8),
            Position(3, 9), Position(4, 9)
        )
    ),

    // patrol_03: bottom-right corridor (row 11), east of the centre door D — every step
    // is adjacent floor (old path used (28,3) which is a wall — enemies clipped through).
    Enemy(
        id = "patrol_03",
        position = Position(23, 11),
        type = EnemyType.PATROL,
        patrolPath = listOf(
            Position(23, 11), Position(24, 11), Position(25, 11), Position(26, 11),
            Position(27, 11), Position(28, 11), Position(29, 11), Position(30, 11),
            Position(29, 11), Position(28, 11), Position(27, 11), Position(26, 11),
            Position(25, 11), Position(24, 11)
        )
    ),

    // ── Random walkers (become chasers ×3.5 during frenzy) ──────────────────
    Enemy(
        id = "random_01",
        position = Position(17, 7),
        type = EnemyType.RANDOM
    ),
    Enemy(
        id = "random_02",
        position = Position(22, 9),
        type = EnemyType.RANDOM
    )
)
