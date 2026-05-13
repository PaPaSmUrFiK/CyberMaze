package com.cybermaze.features.level_10

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cybermaze.core.ui.theme.LevelPalette
import kotlinx.coroutines.delay
import kotlin.math.sin
import kotlin.random.Random

/**
 * One-shot full-screen "CORE BREACHED" glitch animation triggered on Level 10
 * victory before the standard LevelCompleteScreen takes over.
 *
 * Runs for [durationMs] (default 3.2s) or until tapped, then invokes [onFinished].
 */
@Composable
fun VictoryAnimation(
    palette: LevelPalette,
    onFinished: () -> Unit,
    modifier: Modifier = Modifier,
    durationMs: Long = 3200
) {
    val transition = rememberInfiniteTransition(label = "victoryGlitch")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "glitchPhase"
    )
    val scanline by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1700, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scanline"
    )
    var finished by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(durationMs)
        if (!finished) {
            finished = true
            onFinished()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable {
                if (!finished) {
                    finished = true
                    onFinished()
                }
            },
        contentAlignment = Alignment.Center
    ) {
        GlitchCanvas(
            phase = phase,
            scanline = scanline,
            primary = palette.hudTitle,
            accent = palette.hudScore,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            GlitchText(
                text = "CORE BREACHED",
                color = palette.hudTitle,
                shadow = palette.enemyChaser,
                fontSize = 44.sp,
                phase = phase
            )
            GlitchText(
                text = "// SYSTEM COMPROMISED",
                color = palette.hudScore,
                shadow = palette.player,
                fontSize = 16.sp,
                phase = phase + 0.3f
            )
            Text(
                text = "tap to continue",
                color = palette.hudScore.copy(alpha = 0.55f),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun GlitchText(
    text: String,
    color: Color,
    shadow: Color,
    fontSize: androidx.compose.ui.unit.TextUnit,
    phase: Float
) {
    val jitterX = (sin(phase * 12.566f) * 4f)
    val jitterY = (sin(phase * 18.85f + 1.2f) * 2.5f)

    Box(contentAlignment = Alignment.Center) {
        Text(
            text = text,
            color = shadow.copy(alpha = 0.85f),
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = (4 + jitterX).dp, top = (2 + jitterY).dp)
        )
        Text(
            text = text,
            color = color,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun GlitchCanvas(
    phase: Float,
    scanline: Float,
    primary: Color,
    accent: Color,
    modifier: Modifier = Modifier
) {
    // Deterministic per-composition bars so the canvas redraws stay coherent.
    val bars = remember {
        val rng = Random(0xCAFEBABE)
        List(14) {
            BarSpec(
                yRatio = rng.nextFloat(),
                heightRatio = 0.005f + rng.nextFloat() * 0.04f,
                hueShift = rng.nextFloat()
            )
        }
    }

    Canvas(modifier = modifier) {
        // Subtle vertical noise wash
        for (i in 0 until 40) {
            val a = 0.04f + ((i % 7) / 30f)
            drawRect(
                color = primary.copy(alpha = a * 0.18f),
                topLeft = Offset(0f, size.height * i / 40f),
                size = Size(size.width, size.height / 40f)
            )
        }

        // Horizontal "tearing" bars that shift with the phase
        bars.forEach { bar ->
            val offset = (phase + bar.hueShift) % 1f
            val y = (bar.yRatio + offset * 0.15f) % 1f
            val h = size.height * bar.heightRatio
            val color = if (bar.hueShift > 0.5f) accent else primary
            drawRect(
                color = color.copy(alpha = 0.55f),
                topLeft = Offset(0f, size.height * y),
                size = Size(size.width, h)
            )
        }

        // Travelling bright scanline
        val scanY = size.height * scanline
        drawRect(
            color = primary.copy(alpha = 0.35f),
            topLeft = Offset(0f, scanY - 2f),
            size = Size(size.width, 4f)
        )
    }
}

private data class BarSpec(
    val yRatio: Float,
    val heightRatio: Float,
    val hueShift: Float
)
