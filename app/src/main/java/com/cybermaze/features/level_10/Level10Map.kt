package com.cybermaze.features.level_10

/**
 * Final-boss map for Level 10 — "Final Core".
 *
 * Three chokepoint [D]oors; three [K]eys in dead-end alcoves (not on the spawn row).
 * Pickups: hidden [H] shield (upper), [S] speed (mid), [P] slow-field before the final gate.
 * Two warp pairs: mid [A]/[B] (row 5) and long jump [A]/[B] (rows 1 / 11).
 */
@Suppress("SpellCheckingInspection")
val LEVEL_10_MAP: Array<String> = arrayOf(
    "###################################",
    "#Xoo....oo..ooo....ooo..oo..oAo..o#",
    "#.##.#.##.#####D#.#####.##.#.###..#",
    "#H...#....#...#.#.#...#....#.#...K#",
    "#.####.##.#.#.#.#.#.#.#.##.####.#.#",
    "#.#....#..#.#.A.#.B.#.#..#....#.#.#",
    "#.#.####.##.#.#####.#.##.####.#.#.#",
    "#....#...#..#...D...#..#...S..#...#",
    "#.####.###.##.#####.##.###.#.####.#",
    "#.#....#k..#..#...#..#...#K#....#.#",
    "#.#.###.###.P#.#.##.###.####.##.#.#",
    "#ooo..ooo..oooooooooD..ooooPooBoE.#",
    "###################################"
)
