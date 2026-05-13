package com.cybermaze.features.level_10

/**
 * Final-boss map for Level 10 — "Final Core".
 *
 * 35 columns x 13 rows (landscape, per TZ §6.10). Every row is **exactly** 35
 * characters wide; `LevelLoader.validateLayout` will crash the level otherwise.
 *
 * Layout overview:
 * - Six horizontal corridors (rows 1, 3, 5, 7, 9, 11) separated by wall bands
 *   with vertical gaps every 4 columns. Player can travel freely between them.
 * - Teleport pair `A` (col 7) ↔ `B` (col 31) on row 3 — long-range warp.
 * - Power-up `P` on row 5, Speed boost `S` and Shield `H` on row 7.
 * - Energy-rich layout: 67 collectibles total.
 *
 * Keys / doors from the TZ are intentionally omitted: the current
 * [com.cybermaze.core.game.system.MovementSystem.canMove] blocks `D` tiles
 * unconditionally and there is no engine path to consume a key on approach.
 * Re-introducing them requires a small engine change (out of scope here).
 */
@Suppress("SpellCheckingInspection")
val LEVEL_10_MAP: Array<String> = arrayOf(
    //   0         1         2         3
    //   012345678901234567890123456789012345
    "###################################", // 0
    "#Xooo.ooo.ooo.ooo.ooo.ooo.ooo.oooE#", // 1
    "#.###.###.###.###.###.###.###.###.#", // 2
    "#.....A.....o.....o.....o.....B...#", // 3
    "#.###.###.###.###.###.###.###.###.#", // 4
    "#.....o.....o.P...o.....o.....o...#", // 5
    "#.###.###.###.###.###.###.###.###.#", // 6
    "#.o.S.o.o.o.o.o.o.o.o.o.o.o.H.o.o.#", // 7
    "#.###.###.###.###.###.###.###.###.#", // 8
    "#.o.o.o.o.o.o.o.o.o.o.o.o.o.o.o.o.#", // 9
    "#.###.###.###.###.###.###.###.###.#", // 10
    "#.....o.....o.....o.....o.....o...#", // 11
    "###################################"  // 12
)
