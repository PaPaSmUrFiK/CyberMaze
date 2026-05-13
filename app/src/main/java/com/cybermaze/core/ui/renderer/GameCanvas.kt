package com.cybermaze.core.ui.renderer

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.cybermaze.core.game.model.Collectible
import com.cybermaze.core.game.model.CollectibleType
import com.cybermaze.core.game.model.Direction
import com.cybermaze.core.game.model.Enemy
import com.cybermaze.core.game.model.EnemyType
import com.cybermaze.core.game.model.GameState
import com.cybermaze.core.game.model.Player
import com.cybermaze.core.game.model.Position
import com.cybermaze.core.game.model.Tile
import com.cybermaze.core.game.model.TileType
import com.cybermaze.core.ui.theme.LevelPalette
import kotlin.math.PI
import kotlin.math.sin

/**
 * Canvas that renders the entire game state.
 *
 * Colors come from [GameState.palette] — level authors override per-level look by
 * setting `palette = …` on their [com.cybermaze.core.game.engine.LevelDefinition].
 *
 * Set [drawBackground] to `false` if the level draws its own background using
 * [LevelBackgroundLayer] or a custom Composable placed below this canvas in the Box stack.
 */
@Composable
fun GameCanvas(
    gameState: GameState,
    modifier: Modifier = Modifier,
    drawBackground: Boolean = true
) {
    val palette = gameState.palette
    val transition = rememberInfiniteTransition(label = "gameAnimation")
    val pulse by transition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val mapWidth = gameState.map.width
        val mapHeight = gameState.map.height
        val tileSize = minOf(size.width / mapWidth, size.height / mapHeight)
        val offsetX = (size.width - mapWidth * tileSize) / 2f
        val offsetY = (size.height - mapHeight * tileSize) / 2f

        if (drawBackground) {
            drawBackground(palette, offsetX, offsetY, mapWidth, mapHeight, tileSize)
        }

        gameState.map.tiles.forEach { row ->
            row.forEach { tile -> drawTile(tile, palette, tileSize, offsetX, offsetY) }
        }

        drawExit(gameState.map.exitPosition, palette, tileSize, offsetX, offsetY, pulse)

        gameState.collectibles.forEach { collectible ->
            if (!collectible.isCollected) {
                drawCollectible(collectible, palette, tileSize, offsetX, offsetY, pulse)
            }
        }

        gameState.enemies.forEach { enemy ->
            if (enemy.isActive) drawEnemy(enemy, palette, tileSize, offsetX, offsetY, pulse)
        }

        drawPlayer(gameState.player, palette, tileSize, offsetX, offsetY, pulse)
    }
}

// ---------------------------------------------------------------------------
// Background grid
// ---------------------------------------------------------------------------

private fun DrawScope.drawBackground(
    palette: LevelPalette,
    offsetX: Float,
    offsetY: Float,
    mapWidth: Int,
    mapHeight: Int,
    tileSize: Float
) {
    drawRect(color = palette.background)

    val gridColor = palette.grid
    for (x in 0..mapWidth) {
        val px = offsetX + x * tileSize
        drawLine(
            color = gridColor,
            start = Offset(px, offsetY),
            end = Offset(px, offsetY + mapHeight * tileSize),
            strokeWidth = 1f
        )
    }
    for (y in 0..mapHeight) {
        val py = offsetY + y * tileSize
        drawLine(
            color = gridColor,
            start = Offset(offsetX, py),
            end = Offset(offsetX + mapWidth * tileSize, py),
            strokeWidth = 1f
        )
    }
}

// ---------------------------------------------------------------------------
// Tiles
// ---------------------------------------------------------------------------

private fun DrawScope.drawTile(
    tile: Tile,
    palette: LevelPalette,
    tileSize: Float,
    offsetX: Float,
    offsetY: Float
) {
    val x = tile.position.x * tileSize + offsetX
    val y = tile.position.y * tileSize + offsetY

    when (tile.type) {
        TileType.WALL -> {
            drawRect(
                color = palette.wallFill,
                topLeft = Offset(x, y),
                size = Size(tileSize, tileSize)
            )
            drawRect(
                color = palette.wallStroke,
                topLeft = Offset(x, y),
                size = Size(tileSize, tileSize),
                style = Stroke(width = 1.5f)
            )
        }
        TileType.DOOR -> {
            drawRect(
                color = palette.door.copy(alpha = 0.35f),
                topLeft = Offset(x + tileSize * 0.05f, y + tileSize * 0.05f),
                size = Size(tileSize * 0.9f, tileSize * 0.9f)
            )
            drawRect(
                color = palette.door,
                topLeft = Offset(x + tileSize * 0.05f, y + tileSize * 0.05f),
                size = Size(tileSize * 0.9f, tileSize * 0.9f),
                style = Stroke(width = 2f)
            )
        }
        TileType.TELEPORT_A -> drawTeleport(x, y, tileSize, palette.teleportA)
        TileType.TELEPORT_B -> drawTeleport(x, y, tileSize, palette.teleportB)
        TileType.TRAP -> drawTrap(x, y, tileSize, palette.trap)
        else -> Unit
    }
}

private fun DrawScope.drawTeleport(x: Float, y: Float, tileSize: Float, color: Color) {
    val cx = x + tileSize / 2f
    val cy = y + tileSize / 2f
    drawCircle(color = color.copy(alpha = 0.25f), radius = tileSize * 0.45f, center = Offset(cx, cy))
    drawCircle(
        color = color,
        radius = tileSize * 0.35f,
        center = Offset(cx, cy),
        style = Stroke(width = 2f)
    )
    drawCircle(color = color, radius = tileSize * 0.1f, center = Offset(cx, cy))
}

private fun DrawScope.drawTrap(x: Float, y: Float, tileSize: Float, color: Color) {
    val cx = x + tileSize / 2f
    val cy = y + tileSize / 2f
    val r = tileSize * 0.35f
    val diamond = Path().apply {
        moveTo(cx, cy - r)
        lineTo(cx + r, cy)
        lineTo(cx, cy + r)
        lineTo(cx - r, cy)
        close()
    }
    drawPath(diamond, color = color.copy(alpha = 0.5f))
    drawPath(diamond, color = color, style = Stroke(width = 2f))
}

// ---------------------------------------------------------------------------
// Collectibles
// ---------------------------------------------------------------------------

private fun DrawScope.drawCollectible(
    collectible: Collectible,
    palette: LevelPalette,
    tileSize: Float,
    offsetX: Float,
    offsetY: Float,
    phase: Float
) {
    val cx = collectible.position.x * tileSize + tileSize / 2 + offsetX
    val cy = collectible.position.y * tileSize + tileSize / 2 + offsetY

    val color = when (collectible.type) {
        CollectibleType.ENERGY_POINT -> palette.energyPoint
        CollectibleType.KEY -> palette.key
        CollectibleType.POWER_UP -> palette.powerUp
        CollectibleType.SPEED_BOOST -> palette.speedBoost
        CollectibleType.SHIELD -> palette.shield
        CollectibleType.BONUS_STAR -> palette.bonusStar
    }

    val pulse = 0.5f + 0.5f * sin(phase + cx * 0.01f + cy * 0.01f)

    val baseRadius = when (collectible.type) {
        CollectibleType.ENERGY_POINT -> tileSize * 0.14f
        else -> tileSize * 0.28f
    }
    val radius = baseRadius * (0.85f + 0.15f * pulse)

    drawCircle(
        color = color.copy(alpha = 0.18f + 0.18f * pulse),
        radius = radius * 1.9f,
        center = Offset(cx, cy)
    )
    drawCircle(color = color, radius = radius, center = Offset(cx, cy))

    if (collectible.type == CollectibleType.KEY) {
        drawCircle(
            color = palette.background,
            radius = radius * 0.45f,
            center = Offset(cx - radius * 0.25f, cy)
        )
    }
}

// ---------------------------------------------------------------------------
// Player
// ---------------------------------------------------------------------------

private fun DrawScope.drawPlayer(
    player: Player,
    palette: LevelPalette,
    tileSize: Float,
    offsetX: Float,
    offsetY: Float,
    phase: Float
) {
    val (drawX, drawY) = interpolatedTopLeft(
        prev = player.previousPosition,
        curr = player.position,
        timer = player.moveTimer,
        interval = player.effectiveStepInterval(),
        tileSize = tileSize,
        offsetX = offsetX,
        offsetY = offsetY
    )
    val cx = drawX + tileSize / 2f
    val cy = drawY + tileSize / 2f
    val radius = tileSize * 0.36f

    val blink = if (player.isInvincible) {
        0.35f + 0.35f * (0.5f + 0.5f * sin(phase * 4f))
    } else 1f

    if (player.hasShield) {
        drawCircle(
            color = palette.shield.copy(alpha = 0.25f),
            radius = radius * 1.7f,
            center = Offset(cx, cy)
        )
        drawCircle(
            color = palette.shield,
            radius = radius * 1.55f,
            center = Offset(cx, cy),
            style = Stroke(width = 2f),
            alpha = 0.7f
        )
    }

    drawCircle(
        color = palette.player.copy(alpha = 0.4f * blink),
        radius = radius * 1.4f,
        center = Offset(cx, cy)
    )
    drawCircle(
        color = palette.player.copy(alpha = blink),
        radius = radius,
        center = Offset(cx, cy)
    )

    if (player.direction != Direction.NONE) {
        val (dx, dy) = when (player.direction) {
            Direction.UP -> 0f to -radius * 0.55f
            Direction.DOWN -> 0f to radius * 0.55f
            Direction.LEFT -> -radius * 0.55f to 0f
            Direction.RIGHT -> radius * 0.55f to 0f
            Direction.NONE -> 0f to 0f
        }
        drawCircle(
            color = palette.background,
            radius = radius * 0.18f,
            center = Offset(cx + dx, cy + dy)
        )
    }
}

// ---------------------------------------------------------------------------
// Enemies
// ---------------------------------------------------------------------------

private fun DrawScope.drawEnemy(
    enemy: Enemy,
    palette: LevelPalette,
    tileSize: Float,
    offsetX: Float,
    offsetY: Float,
    phase: Float
) {
    // GUARD enemies do not move, so no interpolation needed; for everyone else
    // we lerp the top-left corner based on how much of the current step has elapsed.
    val (x, y) = if (enemy.type == EnemyType.GUARD) {
        enemy.position.x * tileSize + offsetX to enemy.position.y * tileSize + offsetY
    } else {
        interpolatedTopLeft(
            prev = enemy.previousPosition,
            curr = enemy.position,
            timer = enemy.moveTimer,
            interval = enemy.effectiveStepInterval(),
            tileSize = tileSize,
            offsetX = offsetX,
            offsetY = offsetY
        )
    }
    val pad = tileSize * 0.14f
    val side = tileSize - 2 * pad

    val color = when (enemy.type) {
        EnemyType.CHASER -> palette.enemyChaser
        EnemyType.PATROL -> palette.enemyPatrol
        EnemyType.FAST -> palette.enemyFast
        EnemyType.RANDOM -> palette.enemyRandom
        EnemyType.GUARD -> palette.enemyGuard
    }
    val pulse = 0.6f + 0.4f * (0.5f + 0.5f * sin(phase * 1.5f))

    drawRect(
        color = color.copy(alpha = 0.25f * pulse),
        topLeft = Offset(x + pad - 4, y + pad - 4),
        size = Size(side + 8, side + 8)
    )
    drawRect(
        color = color,
        topLeft = Offset(x + pad, y + pad),
        size = Size(side, side)
    )
    drawRect(
        color = Color.White.copy(alpha = 0.6f),
        topLeft = Offset(x + pad, y + pad),
        size = Size(side, side),
        style = Stroke(width = 1.5f)
    )

    val cx = x + tileSize / 2f
    val cy = y + tileSize / 2f
    drawCircle(color = Color.White, radius = side * 0.12f, center = Offset(cx, cy))
    drawCircle(color = color, radius = side * 0.05f, center = Offset(cx, cy))
}

// ---------------------------------------------------------------------------
// Interpolation
// ---------------------------------------------------------------------------

/**
 * Top-left pixel of an entity sliding from [prev] to [curr]. The slide takes
 * exactly [interval] seconds; [timer] is the elapsed time since the step started.
 */
private fun interpolatedTopLeft(
    prev: Position,
    curr: Position,
    timer: Float,
    interval: Float,
    tileSize: Float,
    offsetX: Float,
    offsetY: Float
): Pair<Float, Float> {
    val progress = if (interval <= 0f || interval.isInfinite()) {
        1f
    } else {
        (timer / interval).coerceIn(0f, 1f)
    }
    val x = (prev.x + (curr.x - prev.x) * progress) * tileSize + offsetX
    val y = (prev.y + (curr.y - prev.y) * progress) * tileSize + offsetY
    return x to y
}

// ---------------------------------------------------------------------------
// Exit
// ---------------------------------------------------------------------------

private fun DrawScope.drawExit(
    exitPos: Position,
    palette: LevelPalette,
    tileSize: Float,
    offsetX: Float,
    offsetY: Float,
    phase: Float
) {
    val x = exitPos.x * tileSize + offsetX
    val y = exitPos.y * tileSize + offsetY
    val pulse = 0.5f + 0.5f * sin(phase * 2f)

    drawRect(
        color = palette.exit.copy(alpha = 0.35f + 0.25f * pulse),
        topLeft = Offset(x, y),
        size = Size(tileSize, tileSize)
    )
    drawRect(
        color = palette.exit,
        topLeft = Offset(x + 2, y + 2),
        size = Size(tileSize - 4, tileSize - 4),
        style = Stroke(width = 2f)
    )
    drawRect(
        color = palette.exit.copy(alpha = 0.5f),
        topLeft = Offset(x + tileSize * 0.25f, y + tileSize * 0.25f),
        size = Size(tileSize * 0.5f, tileSize * 0.5f)
    )
}
