package com.cybermaze.core.ui.renderer

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import com.cybermaze.core.ui.theme.LevelPalette
import kotlin.math.PI
import kotlin.math.sin

/**
 * Optional animated background drawn **below** [GameCanvas].
 *
 * Use this when you want a custom look for your level:
 *  1. Pass `drawBackground = false` to [GameCanvas].
 *  2. Place this composable (or your own) below GameCanvas in the same Box stack.
 *
 * Two built-in styles are provided via [LevelBackgroundStyle]; level authors can also
 * pass an entirely custom Composable through [content] when this presets do not fit.
 */
enum class LevelBackgroundStyle { Grid, Radial, ScanLines }

@Composable
fun LevelBackgroundLayer(
    palette: LevelPalette,
    modifier: Modifier = Modifier,
    style: LevelBackgroundStyle = LevelBackgroundStyle.Grid
) {
    val transition = rememberInfiniteTransition(label = "bgPulse")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = (2f * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "bgPhase"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(palette.background)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            when (style) {
                LevelBackgroundStyle.Grid -> {
                    val step = 28f
                    val alpha = 0.10f + 0.05f * (0.5f + 0.5f * sin(phase))
                    val color = palette.grid.copy(alpha = alpha)
                    var x = 0f
                    while (x < size.width) {
                        drawLine(
                            color = color,
                            start = Offset(x, 0f),
                            end = Offset(x, size.height),
                            strokeWidth = 1f
                        )
                        x += step
                    }
                    var y = 0f
                    while (y < size.height) {
                        drawLine(
                            color = color,
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = 1f
                        )
                        y += step
                    }
                }
                LevelBackgroundStyle.Radial -> {
                    val cx = size.width / 2f
                    val cy = size.height / 2f
                    val maxR = kotlin.math.max(size.width, size.height) * 0.7f
                    val rings = 8
                    for (i in 1..rings) {
                        val t = (phase / (2f * PI.toFloat())) + i.toFloat() / rings
                        val frac = (t % 1f)
                        val r = frac * maxR
                        drawCircle(
                            color = palette.grid.copy(alpha = (1f - frac) * 0.18f),
                            radius = r,
                            center = Offset(cx, cy),
                            style = Stroke(width = 1.5f)
                        )
                    }
                }
                LevelBackgroundStyle.ScanLines -> {
                    val offset = (sin(phase) * 12f)
                    val color = palette.grid.copy(alpha = 0.22f)
                    var y = -20f + offset
                    while (y < size.height) {
                        drawLine(
                            color = color,
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = 1f
                        )
                        y += 4f
                    }
                }
            }
        }
    }
}

/**
 * Custom-content overload — if neither of the built-in [LevelBackgroundStyle]s is
 * sufficient, pass arbitrary Composable content. The container draws [palette.background]
 * as a solid base layer for you.
 */
@Composable
fun LevelBackgroundLayer(
    palette: LevelPalette,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(palette.background),
        content = content
    )
}
