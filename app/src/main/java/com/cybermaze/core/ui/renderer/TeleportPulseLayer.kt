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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.cybermaze.core.game.model.GameState
import com.cybermaze.core.game.model.Position
import kotlin.math.PI
import kotlin.math.sin

/**
 * Pulsing rings on teleport pad positions from [GameState.teleports].
 * Colors come from [GameState.palette] (teleportA / teleportB).
 * Brighter / larger pulse when the paired pad is off cooldown.
 */
@Composable
fun TeleportPulseLayer(
    gameState: GameState,
    modifier: Modifier = Modifier
) {
    if (gameState.teleports.isEmpty()) return
    val colorA = gameState.palette.teleportA
    val colorB = gameState.palette.teleportB

    val transition = rememberInfiniteTransition(label = "tpPulse")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = (2f * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "tpPhase"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val m = size.toMapMetrics(gameState.map)
        val pulse = 0.5f + 0.5f * sin(phase)

        gameState.teleports.forEach { tp ->
            drawPair(tp.posA, tp.cooldownA <= 0f, colorA, m, pulse)
            drawPair(tp.posB, tp.cooldownB <= 0f, colorB, m, pulse)
        }
    }
}

private fun DrawScope.drawPair(
    pos: Position,
    ready: Boolean,
    color: Color,
    m: MapCanvasMetrics,
    pulse: Float
) {
    val cx = pos.x * m.tileSize + m.tileSize / 2f + m.offsetX
    val cy = pos.y * m.tileSize + m.tileSize / 2f + m.offsetY
    val base = m.tileSize * (0.32f + 0.08f * pulse) * if (ready) 1.15f else 0.9f
    drawCircle(
        color = color.copy(alpha = if (ready) 0.22f else 0.12f),
        radius = base * 1.6f,
        center = Offset(cx, cy)
    )
    drawCircle(
        color = color.copy(alpha = if (ready) 0.85f else 0.45f),
        radius = base,
        center = Offset(cx, cy),
        style = Stroke(width = 2f)
    )
}
