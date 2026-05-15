package com.cybermaze.features.level_07

import com.cybermaze.core.game.level.LevelLoader

/**
 * Level 7 layout — 31×13; keys in the maze center and a deep wing, one exit choke [D].
 */
@Suppress("SpellCheckingInspection")
val LEVEL_07_MAP: Array<String> = arrayOf(
    "###############################",
    "#Xoo..ooo....ooo....ooo....oo.#",
    "#.##.#####.#####.#####.#####..#",
    "#....#...#.#...#.#ooo#.#ooo#..#",
    "#.####.#.#.#.#.#.#.#.#.#.#.##.#",
    "#.#....#...#.#...#...#.#....#.#",
    "#.#.########.#####.#######.##.#",
    "#...#..A...#.....#...B...#....#",
    "#.###.#####K#.###.#.#####.###D#",
    "#.#.S.#...#ooooo#..K..#...#.#.#",
    "#.#.###.#.##########.#.###.##.#",
    "#ooo..o.o..oo..D.oP..o.o..ooE.#",
    "###############################"
).also {
    val errors = LevelLoader.validateLayout(it)
    check(errors.isEmpty()) { "LEVEL_07_MAP invalid: $errors" }
}
