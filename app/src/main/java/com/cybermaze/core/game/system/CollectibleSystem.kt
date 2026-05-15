package com.cybermaze.core.game.system

import com.cybermaze.core.game.model.Collectible
import com.cybermaze.core.game.model.CollectibleType
import com.cybermaze.core.game.model.GameMap
import com.cybermaze.core.game.model.Player
import com.cybermaze.core.game.model.TileType

/**
 * Builds [Collectible]s from the static map and applies their effects.
 *
 * Note: the underlying tile is *not* changed when an item is collected —
 * we rely on `isCollected` so that the map remains the source of truth for
 * walls / doors / exit.
 */
class CollectibleSystem {

    /**
     * Apply a single collectible. Returns the updated player and the
     * collectibles list with the matching item marked as collected.
     */
    fun collectItem(
        player: Player,
        collectible: Collectible,
        collectibles: List<Collectible>
    ): Pair<Player, List<Collectible>> {
        var updated = player.addScore(collectible.value)

        updated = when (collectible.type) {
            CollectibleType.ENERGY_POINT,
            CollectibleType.POWER_UP,
            CollectibleType.BONUS_STAR -> updated
            CollectibleType.KEY -> updated.collectKey()
            CollectibleType.SPEED_BOOST -> updated.applySpeedBoost(
                SPEED_BOOST_MULT,
                Player.DEFAULT_SPEED_BOOST_DURATION
            )
            CollectibleType.SHIELD -> updated.activateShield(Player.DEFAULT_SHIELD_DURATION)
        }

        val newList = collectibles.map { c ->
            if (c.position == collectible.position && c.type == collectible.type && !c.isCollected) {
                c.collect()
            } else c
        }
        return updated to newList
    }

    fun getUncollected(collectibles: List<Collectible>): List<Collectible> =
        collectibles.filter { !it.isCollected }

    fun countUncollected(collectibles: List<Collectible>, type: CollectibleType): Int =
        collectibles.count { !it.isCollected && it.type == type }

    /**
     * Build [Collectible]s for every collectible tile on the [map].
     * Order is row-major, deterministic.
     */
    fun createCollectiblesFromMap(map: GameMap): List<Collectible> {
        val out = mutableListOf<Collectible>()
        map.tiles.forEach { row ->
            row.forEach { tile ->
                val type = when (tile.type) {
                    TileType.ENERGY_POINT -> CollectibleType.ENERGY_POINT
                    TileType.KEY -> CollectibleType.KEY
                    TileType.POWER_UP -> CollectibleType.POWER_UP
                    TileType.SPEED_BOOST -> CollectibleType.SPEED_BOOST
                    TileType.SHIELD -> CollectibleType.SHIELD
                    else -> null
                }
                if (type != null) {
                    out += Collectible(position = tile.position, type = type)
                }
            }
        }
        return out
    }

    companion object {
        /** Multiplier applied to the player when a SPEED_BOOST is picked up. */
        const val SPEED_BOOST_MULT: Float = 1.5f
    }
}
