package com.cybermaze.core.game.system

import com.cybermaze.core.game.model.Enemy
import com.cybermaze.core.game.model.GameMap
import com.cybermaze.core.game.model.Position

/**
 * Drives all enemies forward by [deltaTime]. Each enemy ticks independently,
 * moving only when its individual timer crosses (1 / speed) seconds.
 */
class EnemySystem(
    private val movementSystem: MovementSystem = MovementSystem()
) {

    fun updateEnemies(
        enemies: List<Enemy>,
        playerPos: Position,
        map: GameMap,
        deltaTime: Float
    ): List<Enemy> = enemies.map { updateEnemy(it, playerPos, map, deltaTime) }

    private fun updateEnemy(
        enemy: Enemy,
        playerPos: Position,
        map: GameMap,
        deltaTime: Float
    ): Enemy {
        if (!enemy.isActive) return enemy
        val ticked = enemy.updateTimer(deltaTime)
        if (!ticked.canMove()) return ticked
        val moved = movementSystem.moveEnemy(ticked, map, playerPos)
        return moved.resetTimer()
    }

    fun setEnemyActive(enemies: List<Enemy>, enemyId: String, active: Boolean): List<Enemy> =
        enemies.map { if (it.id == enemyId) it.setActive(active) else it }

    fun getActiveEnemies(enemies: List<Enemy>): List<Enemy> = enemies.filter { it.isActive }
}
