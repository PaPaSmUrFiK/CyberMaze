package com.cybermaze.core.game.model

/**
 * Represents a single tile on the game map.
 */
data class Tile(
    val type: TileType,
    val position: Position,
    val isPassable: Boolean = type.isPassable(),
    val metadata: Map<String, Any> = emptyMap()
) {
    /**
     * Returns a copy of this tile with updated passability.
     * Useful for opening doors.
     */
    fun withPassable(passable: Boolean): Tile = copy(isPassable = passable)
    
    /**
     * Returns a copy of this tile with a different type.
     */
    fun withType(newType: TileType): Tile = copy(
        type = newType,
        isPassable = newType.isPassable()
    )
}
