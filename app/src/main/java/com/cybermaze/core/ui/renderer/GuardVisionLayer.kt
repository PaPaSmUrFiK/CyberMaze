package com.cybermaze.core.ui.renderer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import com.cybermaze.core.game.model.Direction
import com.cybermaze.core.game.model.EnemyType
import com.cybermaze.core.game.model.GameState

private fun directionUnit(dir: Direction): Offset = when (dir) {
    Direction.RIGHT -> Offset(1f, 0f)
    Direction.LEFT -> Offset(-1f, 0f)
    Direction.DOWN -> Offset(0f, 1f)
    Direction.UP -> Offset(0f, -1f)
    Direction.NONE -> Offset(1f, 0f)
}

/**
 * Semi-transparent vision cones for [EnemyType.GUARD] enemies (3 tiles ahead).
 * Color comes from [GameState.palette].guardVision.
 */
@Composable
fun GuardVisionLayer(
    gameState: GameState,
    modifier: Modifier = Modifier,
    visionTiles: Int = 3
) {
    val guards = gameState.enemies.filter { it.type == EnemyType.GUARD && it.isActive }
    if (guards.isEmpty()) return
    val visionColor = gameState.palette.guardVision

    Canvas(modifier = modifier.fillMaxSize()) {
        val m = size.toMapMetrics(gameState.map)
        val du = m.tileSize

        guards.forEach { g ->
            val base = m.tileCenter(g.position)
            val u = directionUnit(g.lookDirection)
            val perp = Offset(-u.y, u.x)

            val nearL = base + u * (du * 0.15f) + perp * (du * 0.42f)
            val nearR = base + u * (du * 0.15f) - perp * (du * 0.42f)
            val farC = base + u * (du * (visionTiles + 0.5f))
            val farL = farC + perp * (du * 0.55f)
            val farR = farC - perp * (du * 0.55f)

            val path = Path().apply {
                moveTo(nearL.x, nearL.y)
                lineTo(farL.x, farL.y)
                lineTo(farR.x, farR.y)
                lineTo(nearR.x, nearR.y)
                close()
            }
            drawPath(
                path = path,
                color = visionColor.copy(alpha = 0.22f),
                style = Fill
            )
        }
    }
}
