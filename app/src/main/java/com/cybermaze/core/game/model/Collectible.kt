package com.cybermaze.core.game.model

/**
 * Represents a collectible item on the map.
 */
data class Collectible(
    val position: Position,
    val type: CollectibleType,
    val value: Int = type.scoreValue(),
    val isCollected: Boolean = false
) {
    /**
     * Returns a new collectible marked as collected.
     */
    fun collect(): Collectible = copy(isCollected = true)
}
