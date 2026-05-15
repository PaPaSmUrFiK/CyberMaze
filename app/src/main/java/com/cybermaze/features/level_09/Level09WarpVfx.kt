package com.cybermaze.features.level_09

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import com.cybermaze.core.game.model.GameState
import com.cybermaze.core.game.model.Position
import com.cybermaze.core.ui.renderer.toMapMetrics
import kotlin.math.PI
import kotlin.math.sin

/**
 * Level 9-only overlay: one thin pulsing ring on the decoy pad during warp preview
 * ([TeleportPulseLayer]-style radius/alpha, pad B tint). No arc — decoy reads as a teleport hint only.
 */
@Composable
fun Level09WarpVfx(
    gameState: GameState,
    decoyPosition: Position?,
    isWarpPreview: Boolean,
    modifier: Modifier = Modifier
) {
    val colorB = gameState.palette.teleportB
    val transition = rememberInfiniteTransition(label = "lvl09decoy")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = (2f * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(650, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "decoyPhase"
    )

    Canvas(modifier = modifier) {
        if (!isWarpPreview || decoyPosition == null) return@Canvas
        val m = size.toMapMetrics(gameState.map)
        val pulse = 0.5f + 0.5f * sin(phase)
        val cx = decoyPosition.x * m.tileSize + m.tileSize / 2f + m.offsetX
        val cy = decoyPosition.y * m.tileSize + m.tileSize / 2f + m.offsetY
        val base = m.tileSize * (0.32f + 0.08f * pulse)
        drawCircle(
            color = colorB.copy(alpha = 0.38f + 0.22f * pulse),
            radius = base,
            center = Offset(cx, cy),
            style = Stroke(width = 2f)
        )
    }
}
