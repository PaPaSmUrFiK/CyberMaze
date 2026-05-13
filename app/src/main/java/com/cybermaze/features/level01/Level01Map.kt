package com.cybermaze.features.level01

/**
 * Map layout for Level 01 — "Boot Sector".
 *
 * A wide-open tutorial maze: 27 columns × 11 rows. Plenty of energy points,
 * a single slow patrol enemy, no keys / no teleports.
 *
 * Legend (see [com.cybermaze.core.game.level.LevelLoader]):
 *   # wall    . empty   o energy point    X spawn    E exit
 *
 * Every row MUST be exactly 27 characters wide — LevelLoader rejects ragged
 * layouts. The double border of `#` keeps the player inside the playfield.
 */
val LEVEL_01_MAP: Array<String> = arrayOf(
    //   0         1         2
    //   0123456789012345678901234567
    "###########################", // 0
    "#X.ooo.ooo.ooo.ooo.ooo.ooE#", // 1
    "#.###.###.###.###.###.###.#", // 2
    "#.....o...o...o...o.......#", // 3
    "#.###.#.#.#.#.#.#.#.#.###.#", // 4
    "#.o...o.o.o...o.o.o...o...#", // 5
    "#.###.#.#.#.#.#.#.#.#.###.#", // 6
    "#.....o...o...o...o.......#", // 7
    "#.###.###.###.###.###.###.#", // 8
    "#.o..ooo.ooo.ooo.ooo.ooo..#", // 9
    "###########################"  // 10
)
