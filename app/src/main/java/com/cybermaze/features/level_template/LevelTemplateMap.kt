package com.cybermaze.features.level_template

/**
 * Example map showing every tile type supported by [com.cybermaze.core.game.level.LevelLoader].
 *
 * Legend (each character → one tile):
 * ```
 * #  wall
 * .  empty walkable
 * o  energy point (collectible)
 * X  player spawn (exactly one)
 * E  exit (exactly one)
 * K  key tile (collectible)
 * D  door (needs key to open)
 * A  teleport pad A (pair with B in [LevelTemplateTeleports])
 * B  teleport pad B
 * T  static trap marker on map (visual only here — moving traps use [LevelTemplateTraps])
 * P  power-up (slows enemies + extends fog vision when fog is enabled)
 * S  speed boost (player moves faster for a few seconds)
 * H  shield pickup
 * ```
 *
 * **Rules:** every row must have the **same width**. You need at least one `X` and one `E`.
 * Validate in debug: `require(LevelLoader.validateLayout(LEVEL_TEMPLATE_MAP).isEmpty())`.
 */
@Suppress("SpellCheckingInspection")
val LEVEL_TEMPLATE_MAP: Array<String> = arrayOf(
    // 0         1         2         3
    // 0123456789012345678901234567890
    "################################", // 0
    "#X.o.o.o.o.o.o.o.o.o.o.o.o.o.E#", // 1
    "#.###.###.###.###.###.###.###.#", // 2
    "#...K...D...A...B...T...P.S.H..#", // 3
    "#.###.#.#.#.#.#.#.#.#.#.#.###.#", // 4
    "#.....o...o...o...o...........#", // 5
    "#.###.###.###.###.###.###.###.#", // 6
    "#.............................#", // 7
    "#.o..ooo.ooo.ooo.ooo.ooo.ooo..#", // 8
    "################################" // 9
)
