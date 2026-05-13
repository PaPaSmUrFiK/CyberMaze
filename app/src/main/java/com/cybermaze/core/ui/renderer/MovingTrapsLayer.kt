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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import com.cybermaze.core.game.model.GameState
import kotlin.math.PI
import kotlin.math.sin

private const val WARN_BEFORE_STEP = 0.2f

/**
 * Pulsing diamonds on [GameState.traps] plus a short "about to step" warning pulse.
 * Color comes from [GameState.palette].trap.
 */
@Composable
fun MovingTrapsLayer(
    gameState: GameState,
    modifier: Modifier = Modifier
) {
    if (gameState.traps.isEmpty()) return
    val trapColor = gameState.palette.trap

    val transition = rememberInfiniteTransition(label = "trapPulse")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = (2f * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "trapPhase"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val m = size.toMapMetrics(gameState.map)
        val pulse = 0.55f + 0.45f * (0.5f + 0.5f * sin(phase))

        gameState.traps.forEach { trap ->
            val cx = trap.position.x * m.tileSize + m.tileSize / 2f + m.offsetX
            val cy = trap.position.y * m.tileSize + m.tileSize / 2f + m.offsetY
            val r = m.tileSize * 0.38f

            val warn =
                trap.moveTimer >= (trap.moveInterval - WARN_BEFORE_STEP) &&
                    trap.moveTimer < trap.moveInterval
            val alphaBoost = if (warn) 0.35f else 0f

            val diamond = Path().apply {
                moveTo(cx, cy - r)
                lineTo(cx + r, cy)
                lineTo(cx, cy + r)
                lineTo(cx - r, cy)
                close()
            }
            drawPath(
                diamond,
                color = trapColor.copy(alpha = 0.45f * pulse + alphaBoost)
            )
            drawPath(
                diamond,
                color = trapColor.copy(alpha = (0.9f + alphaBoost).coerceAtMost(1f)),
                style = Stroke(width = 2.5f)
            )
        }
    }
}
