package com.cybermaze.core.game.system

import com.cybermaze.core.game.model.Collectible
import com.cybermaze.core.game.model.Enemy
import com.cybermaze.core.game.model.GameMap
import com.cybermaze.core.game.model.Player
import com.cybermaze.core.game.model.Position
import com.cybermaze.core.game.model.Tile
import com.cybermaze.core.game.model.TileType

/**
 * Detects and resolves collisions between the player and the rest of the world
 * (enemies, collectibles, doors, exit).
 */
class CollisionSystem {

    /** Any active enemy currently sharing the player's tile. */
    fun checkPlayerEnemyCollision(player: Player, enemies: List<Enemy>): Boolean =
        enemies.any { it.isActive && it.position == player.position }

    /** All collectibles standing on the player's tile that haven't been picked up. */
    fun checkPlayerCollectible(player: Player, collectibles: List<Collectible>): List<Collectible> =
        collectibles.filter { !it.isCollected && it.position == player.position }

    fun checkPlayerTile(player: Player, map: GameMap): TileType? =
        map.getTile(player.position)?.type

    /**
     * Apply enemy contact: shield absorbs the hit, otherwise the player
     * loses a life and gets an invincibility window.
     */
    fun handleEnemyCollision(player: Player): Player {
        if (player.isInvincible) return player
        return if (player.hasShield) {
            player.removeShield().grantInvincibility()
        } else {
            player.loseLife().grantInvincibility()
        }
    }

    fun canOpenDoor(player: Player, doorPosition: Position, map: GameMap): Boolean {
        val tile = map.getTile(doorPosition) ?: return false
        return tile.type == TileType.DOOR && player.keysCollected > 0
    }

    /** Use a key and turn the door into an EMPTY tile on a copy of the map. */
    fun openDoor(
        player: Player,
        doorPosition: Position,
        map: GameMap
    ): Pair<Player, GameMap> {
        val tile = map.getTile(doorPosition) ?: return player to map
        if (tile.type != TileType.DOOR || player.keysCollected <= 0) return player to map
        val updatedPlayer = player.useKey()
        val openTile = Tile(type = TileType.EMPTY, position = doorPosition)
        return updatedPlayer to map.updateTile(doorPosition, openTile)
    }

    fun hasReachedExit(player: Player, map: GameMap): Boolean =
        player.position == map.exitPosition

    fun getCollidingEnemies(player: Player, enemies: List<Enemy>): List<Enemy> =
        enemies.filter { it.isActive && it.position == player.position }
}
