package com.cybermaze.features.level_09

/**
 * Level 9 — "Deep Maze" (33×13).
 *
 * Progression:
 *  1. Left section: collect K1 at (4,9) near patrol_01, open D1 at (4,5) → reach portal A.
 *  2. Portal A→B (mandatory bridge between halves).
 *  3. Right section: K2 at (31,2) — +1 right, +1 down from exit E(30,1); open D2 at (29,1) → E.
 *  4. K3 at (25,11) on patrol_03 path unlocks D3 at (21,11).
 *  Shield (H) removed; portals become hazardous every 15 s instead.
 */
@Suppress("SpellCheckingInspection")
val LEVEL_09_MAP: Array<String> = arrayOf(
    "#################################",
    "#Xo..o..o..o....#o...o..o...oDE##", // D(29,1) locks exit E(30,1) — top-right dead-end
    "#.###.#####.###.#.###.#####.###K#", // K2(31,2): +1x +1y from E(30,1)
    "#.#o..#...#..o#.#.#o..#...#..o#.#",
    "#.#.###.#.###.#.#.#.###.#.###.#.#",
    "#.#.DA..#.....#.#.....#..B..#.#.#", // D(4,5) blocks portal A; B(25,5)
    "#.###.#.#####.#.#.#####.#.###.#.#",
    "#..o#.#..o..#...#..o#..o..#.#..o#",
    "#.#.#.#####.#.#.#.#.#.#####.#.#.#",
    "#.#.K..#....#...#...#..o.o#...#.#",  // K1(4,9) near patrol_01; was K3(23,9) → moved to (25,11)
    "#.###.#.###.#.#.#.#.###.#.###.#.#",
    "#o..o.o.....o..#..o..D..oKo..o..#",  // K3(25,11) on patrol_03; D3(21,11)
    "#################################"
)
