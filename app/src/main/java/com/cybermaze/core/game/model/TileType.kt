package com.cybermaze.core.game.model

/**
 * Types of tiles that can exist on the game map.
 * Each tile type has different properties and behaviors.
 */
enum class TileType {
    EMPTY,           // Empty walkable space
    WALL,            // Solid wall - blocks movement
    ENERGY_POINT,    // Collectible energy point
    DOOR,            // Door - requires key to open
    KEY,             // Key - opens doors
    EXIT,            // Level exit
    TELEPORT_A,      // Teleport point A
    TELEPORT_B,      // Teleport point B
    TRAP,            // Trap - damages player
    POWER_UP,        // Power-up - slows enemies
    SPEED_BOOST,     // Speed boost for player
    SHIELD,          // Shield - protects from one hit
    DARKNESS_ZONE,   // Area with reduced visibility
    SPAWN_POINT;     // Player spawn location
    
    /**
     * Returns true if this tile can be walked through.
     */
    fun isPassable(): Boolean = when (this) {
        WALL, DOOR -> false
        else -> true
    }
    
    /**
     * Returns true if this tile is a collectible item.
     */
    fun isCollectible(): Boolean = when (this) {
        ENERGY_POINT, KEY, POWER_UP, SPEED_BOOST, SHIELD -> true
        else -> false
    }
}
