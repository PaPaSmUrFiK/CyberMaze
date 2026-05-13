package com.cybermaze.features.menu

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cybermaze.core.ui.components.NeonButton
import com.cybermaze.core.ui.theme.CyberColors

/**
 * Title screen with PLAY / SETTINGS / ABOUT buttons.
 *
 * A subtle pulsing grid in the background gives it some cyber atmosphere
 * without dragging in heavyweight assets.
 */
@Composable
fun MainMenuScreen(
    onPlayClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onAboutClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberColors.Background)
    ) {
        BackgroundGrid()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "CYBER MAZE",
                color = CyberColors.NeonCyan,
                fontSize = 56.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "DIGITAL LABYRINTH",
                color = CyberColors.TextSecondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(36.dp))

            Box(modifier = Modifier.width(220.dp)) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    NeonButton(text = "PLAY", onClick = onPlayClick, color = CyberColors.NeonGreen)
                    NeonButton(text = "SETTINGS", onClick = onSettingsClick)
                    NeonButton(text = "ABOUT", onClick = onAboutClick, color = CyberColors.NeonPink)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "v1.0.0",
                color = CyberColors.TextSecondary.copy(alpha = 0.5f),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun BackgroundGrid() {
    val transition = rememberInfiniteTransition(label = "menuGrid")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(animation = tween(4500), repeatMode = RepeatMode.Reverse),
        label = "menuPhase"
    )
    Canvas(modifier = Modifier.fillMaxSize()) {
        val step = 40f
        val cyan = CyberColors.NeonCyan.copy(alpha = 0.06f + 0.04f * phase)
        var x = 0f
        while (x < size.width) {
            drawLine(color = cyan, start = Offset(x, 0f), end = Offset(x, size.height), strokeWidth = 1f)
            x += step
        }
        var y = 0f
        while (y < size.height) {
            drawLine(color = cyan, start = Offset(0f, y), end = Offset(size.width, y), strokeWidth = 1f)
            y += step
        }
        // Subtle vignette
        drawRect(
            color = CyberColors.Background.copy(alpha = 0.4f),
            topLeft = Offset.Zero,
            size = Size(size.width, size.height)
        )
    }
}
