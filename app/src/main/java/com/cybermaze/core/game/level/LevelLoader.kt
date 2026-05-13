package com.cybermaze.core.game.level

import com.cybermaze.core.game.model.*

/**
 * Loads game levels from string array representations.
 * This is the core system that students will use to define their level maps.
 * 
 * Map Legend:
 * # = Wall
 * . = Empty space
 * o = Energy point
 * X = Player spawn
 * E = Exit
 * K = Key
 * D = Door
 * A = Teleport A
 * B = Teleport B
 * T = Trap
 * P = Power-up
 * S = Speed boost
 * H = Shield
 */
object LevelLoader {
    
    /**
     * Loads a game map from a string array.
     * Each string represents one row of the map.
     * 
     * Example:
     * ```
     * val map = arrayOf(
     *     "###########",
     *     "#X.o.o.o.E#",
     *     "###########"
     * )
     * ```
     */
    fun loadFromStringArray(
        layout: Array<String>,
        config: LevelConfig
    ): GameMap {
        if (layout.isEmpty()) {
            throw IllegalArgumentException("Level layout cannot be empty")
        }
        
        val height = layout.size
        val width = layout[0].length
        
        // Validate all rows have same width
        layout.forEach { row ->
            if (row.length != width) {
                throw IllegalArgumentException("All rows must have the same width")
            }
        }
        
        var playerSpawn = Position(0, 0)
        var exitPos = Position(width - 1, height - 1)
        
        // Parse the layout into tiles
        val tiles = Array(height) { y ->
            Array(width) { x ->
                val char = layout[y].getOrNull(x) ?: '.'
                val tileType = charToTileType(char)
                
                // Track special positions
                when (tileType) {
                    TileType.SPAWN_POINT -> playerSpawn = Position(x, y)
                    TileType.EXIT -> exitPos = Position(x, y)
                    else -> {}
                }
                
                Tile(
                    type = tileType,
                    position = Position(x, y)
                )
            }
        }
        
        return GameMap(
            width = width,
            height = height,
            tiles = tiles,
            playerSpawn = playerSpawn,
            exitPosition = exitPos
        )
    }
    
    /**
     * Converts a character to a tile type.
     * Students can extend this mapping for custom tiles.
     */
    private fun charToTileType(char: Char): TileType = when (char) {
        '#'  -> TileType.WALL
        '.'  -> TileType.EMPTY
        'o'  -> TileType.ENERGY_POINT
        'D'  -> TileType.DOOR
        'K'  -> TileType.KEY
        'E'  -> TileType.EXIT
        'A'  -> TileType.TELEPORT_A
        'B'  -> TileType.TELEPORT_B
        'T'  -> TileType.TRAP
        'P'  -> TileType.POWER_UP
        'S'  -> TileType.SPEED_BOOST
        'H'  -> TileType.SHIELD
        'X'  -> TileType.SPAWN_POINT
        else -> TileType.EMPTY
    }
    
    /**
     * Validates a level layout before loading.
     * Returns list of validation errors, empty if valid.
     */
    fun validateLayout(layout: Array<String>): List<String> {
        val errors = mutableListOf<String>()
        
        if (layout.isEmpty()) {
            errors.add("Layout is empty")
            return errors
        }
        
        val width = layout[0].length
        
        // Check all rows have same width
        layout.forEachIndexed { index, row ->
            if (row.length != width) {
                errors.add("Row $index has different width: ${row.length} vs $width")
            }
        }
        
        // Check for player spawn
        val hasSpawn = layout.any { row -> row.contains('X') }
        if (!hasSpawn) {
            errors.add("No player spawn point (X) found")
        }
        
        // Check for exit
        val hasExit = layout.any { row -> row.contains('E') }
        if (!hasExit) {
            errors.add("No exit point (E) found")
        }
        
        return errors
    }
    
    /**
     * Counts specific tile types in a layout.
     * Useful for validating level design.
     */
    fun countTiles(layout: Array<String>, char: Char): Int {
        return layout.sumOf { row -> row.count { it == char } }
    }
}
