package com.cybermaze.core.game.system

import com.cybermaze.core.game.model.Direction
import com.cybermaze.core.game.model.Enemy
import com.cybermaze.core.game.model.EnemyType
import com.cybermaze.core.game.model.GameMap
import com.cybermaze.core.game.model.Player
import com.cybermaze.core.game.model.Position
import com.cybermaze.core.game.model.TileType

/**
 * Handles movement for the player and enemy AI.
 *
 * The system is intentionally stateless — given a snapshot it returns a new
 * snapshot. Enemies use the [Enemy.type] to choose between patrol / chase /
 * random behaviour.
 */
class MovementSystem {

    /**
     * Whether the tile in [direction] from [pos] is in-bounds and walkable.
     * Doors are passable from this check ONLY when the caller already used
     * a key — to keep things simple this function treats them as blocked.
     */
    fun canMove(pos: Position, direction: Direction, map: GameMap): Boolean {
        if (direction == Direction.NONE) return false
        val newPos = pos.moved(direction)
        return map.isInBounds(newPos) && map.isPassable(newPos)
    }

    /** Move the player one tile if possible; otherwise return the same player. */
    fun movePlayer(player: Player, direction: Direction, map: GameMap): Player {
        if (direction == Direction.NONE) return player
        if (!canMove(player.position, direction, map)) return player.withDirection(direction)
        return player.movedTo(player.position.moved(direction), direction)
    }

    /** Update enemy position according to its AI behaviour. */
    fun moveEnemy(enemy: Enemy, map: GameMap, playerPos: Position): Enemy {
        if (!enemy.isActive) return enemy
        return when (enemy.type) {
            EnemyType.PATROL -> movePatrol(enemy)
            EnemyType.CHASER -> moveChaser(enemy, map, playerPos)
            EnemyType.RANDOM -> moveRandom(enemy, map)
            EnemyType.GUARD -> enemy
            EnemyType.FAST -> moveRandom(enemy, map)
        }
    }

    private fun movePatrol(enemy: Enemy): Enemy {
        if (enemy.patrolPath.isEmpty()) return enemy
        return enemy.advancePatrol()
    }

    private fun moveChaser(enemy: Enemy, map: GameMap, target: Position): Enemy {
        val dx = target.x - enemy.position.x
        val dy = target.y - enemy.position.y

        val primary = if (kotlin.math.abs(dx) >= kotlin.math.abs(dy)) {
            if (dx > 0) Direction.RIGHT else if (dx < 0) Direction.LEFT else Direction.NONE
        } else {
            if (dy > 0) Direction.DOWN else if (dy < 0) Direction.UP else Direction.NONE
        }
        val secondary = if (kotlin.math.abs(dx) >= kotlin.math.abs(dy)) {
            if (dy > 0) Direction.DOWN else if (dy < 0) Direction.UP else Direction.NONE
        } else {
            if (dx > 0) Direction.RIGHT else if (dx < 0) Direction.LEFT else Direction.NONE
        }

        for (dir in listOf(primary, secondary)) {
            if (dir != Direction.NONE && canMove(enemy.position, dir, map)) {
                return enemy.movedTo(enemy.position.moved(dir), dir)
            }
        }
        return moveRandom(enemy, map)
    }

    private fun moveRandom(enemy: Enemy, map: GameMap): Enemy {
        val valid = Direction.entries
            .filter { it != Direction.NONE }
            .filter { canMove(enemy.position, it, map) }
        if (valid.isEmpty()) return enemy
        // Bias towards continuing in the current direction when possible — gives
        // a more believable, less twitchy wander pattern.
        val preferred = if (enemy.direction in valid && Math.random() < 0.7) enemy.direction
        else valid.random()
        return enemy.movedTo(enemy.position.moved(preferred), preferred)
    }

    /** All directions a unit can step from [pos]. Useful for AI extensions. */
    fun getValidDirections(pos: Position, map: GameMap): List<Direction> =
        Direction.entries
            .filter { it != Direction.NONE }
            .filter { canMove(pos, it, map) }

    /** Manhattan-style dominant direction from [from] to [to]. */
    fun getDirectionTo(from: Position, to: Position): Direction {
        val dx = to.x - from.x
        val dy = to.y - from.y
        return when {
            kotlin.math.abs(dx) > kotlin.math.abs(dy) -> if (dx > 0) Direction.RIGHT else Direction.LEFT
            dy > 0 -> Direction.DOWN
            dy < 0 -> Direction.UP
            else -> Direction.NONE
        }
    }

    /** True if the player is standing on a door tile that they may open. */
    fun isFacingOpenableDoor(player: Player, map: GameMap): Boolean {
        val tile = map.getTile(player.position) ?: return false
        return tile.type == TileType.DOOR && player.keysCollected > 0
    }
}
