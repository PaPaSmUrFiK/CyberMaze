package com.cybermaze.core.game.system

import com.cybermaze.core.game.engine.GameEvent
import com.cybermaze.core.game.engine.GameEventEmitter
import com.cybermaze.core.game.engine.HazardDamage
import com.cybermaze.core.game.engine.LevelSystem
import com.cybermaze.core.game.model.CollectibleType
import com.cybermaze.core.game.model.Direction
import com.cybermaze.core.game.model.Enemy
import com.cybermaze.core.game.model.EnemyType
import com.cybermaze.core.game.model.GameMap
import com.cybermaze.core.game.model.GameState
import com.cybermaze.core.game.model.Position
import com.cybermaze.core.game.model.TileType

/**
 * [EnemyType.GUARD] stands still, rotates every 3s, and damages the player if they stand
 * in a straight-line vision cone, with walls blocking line-of-sight.
 *
 * @param damageRange how many tiles ahead can deal damage (default 3). [GuardVisionLayer]
 * still draws [visionRange] tiles for warning.
 */
class GuardSystem(
    private val collisionSystem: CollisionSystem,
    private val damageRange: Int = 3
) : LevelSystem {

    private val rotationPeriod: Float = 3f

    init {
        require(damageRange in 1..3) { "damageRange must be 1..3" }
    }

    override fun update(state: GameState, deltaTime: Float, emit: GameEventEmitter): GameState {
        val guards = state.enemies.filter { it.type == EnemyType.GUARD && it.isActive }
        if (guards.isEmpty()) return state

        val rotated = state.enemies.map { enemy ->
            if (enemy.type != EnemyType.GUARD || !enemy.isActive) return@map enemy
            var rot = enemy.rotationTimer + deltaTime
            var look = enemy.lookDirection
            if (rot >= rotationPeriod) {
                rot = 0f
                look = look.rotateClockwise()
            }
            enemy.copy(rotationTimer = rot, lookDirection = look)
        }

        var next = state.updateEnemies(rotated)
        val playerPos = next.player.position

        for (enemy in next.enemies.filter { it.type == EnemyType.GUARD && it.isActive }) {
            if (next.player.isInvincible) break
            if (isPlayerInGuardSight(enemy, playerPos, next.map)) {
                val beforeLives = next.player.lives
                next = HazardDamage.applyPlayerHit(next, collisionSystem)
                emit(GameEvent.HazardHit)
                emit(GameEvent.PlayerHit(next.player.lives))
                if (next.player.lives < beforeLives) {
                    if (next.player.lives <= 0) emit(GameEvent.PlayerDied) else emit(GameEvent.PlayerRespawned)
                }
                break
            }
        }
        return next
    }

    private fun isPlayerInGuardSight(guard: Enemy, playerPos: Position, map: GameMap): Boolean {
        val g = guard.position
        val dir = guard.lookDirection
        if (dir == Direction.NONE) return false

        for (step in 1..damageRange) {
            val cell = when (dir) {
                Direction.RIGHT -> Position(g.x + step, g.y)
                Direction.LEFT -> Position(g.x - step, g.y)
                Direction.DOWN -> Position(g.x, g.y + step)
                Direction.UP -> Position(g.x, g.y - step)
                Direction.NONE -> return false
            }
            if (!map.isInBounds(cell)) return false
            val tile = map.getTile(cell)?.type ?: return false
            if (tile == TileType.WALL || tile == TileType.DOOR) return false
            if (cell == playerPos) return true
        }
        return false
    }

    private fun Direction.rotateClockwise(): Direction = when (this) {
        Direction.UP -> Direction.RIGHT
        Direction.RIGHT -> Direction.DOWN
        Direction.DOWN -> Direction.LEFT
        Direction.LEFT -> Direction.UP
        Direction.NONE -> Direction.RIGHT
    }

    override fun onCollectibleCollected(
        state: GameState,
        type: CollectibleType,
        position: Position,
        emit: GameEventEmitter
    ): GameState = state
}
