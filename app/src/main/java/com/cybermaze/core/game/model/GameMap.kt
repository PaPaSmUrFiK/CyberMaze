package com.cybermaze.core.game.model

/**
 * Represents the game map/level.
 * Contains all tiles and level metadata.
 */
data class GameMap(
    val width: Int,
    val height: Int,
    val tiles: Array<Array<Tile>>,
    val playerSpawn: Position,
    val exitPosition: Position
) {
    /**
     * Gets the tile at the specified position.
     * Returns null if position is out of bounds.
     */
    fun getTile(pos: Position): Tile? {
        return tiles.getOrNull(pos.y)?.getOrNull(pos.x)
    }
    
    /**
     * Checks if a position is passable (can be walked through).
     */
    fun isPassable(pos: Position): Boolean {
        return getTile(pos)?.isPassable == true
    }
    
    /**
     * Checks if a position is within map bounds.
     */
    fun isInBounds(pos: Position): Boolean {
        return pos.x in 0 until width && pos.y in 0 until height
    }
    
    /**
     * Updates a tile at the specified position.
     * Returns a new GameMap with the updated tile.
     */
    fun updateTile(pos: Position, newTile: Tile): GameMap {
        if (!isInBounds(pos)) return this
        
        val newTiles = tiles.map { row -> row.clone() }.toTypedArray()
        newTiles[pos.y][pos.x] = newTile
        
        return copy(tiles = newTiles)
    }
    
    /**
     * Finds all tiles of a specific type.
     */
    fun findTiles(type: TileType): List<Tile> {
        return tiles.flatMap { row ->
            row.filter { it.type == type }
        }
    }
    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        
        other as GameMap
        
        if (width != other.width) return false
        if (height != other.height) return false
        if (!tiles.contentDeepEquals(other.tiles)) return false
        if (playerSpawn != other.playerSpawn) return false
        if (exitPosition != other.exitPosition) return false
        
        return true
    }
    
    override fun hashCode(): Int {
        var result = width
        result = 31 * result + height
        result = 31 * result + tiles.contentDeepHashCode()
        result = 31 * result + playerSpawn.hashCode()
        result = 31 * result + exitPosition.hashCode()
        return result
    }
}
