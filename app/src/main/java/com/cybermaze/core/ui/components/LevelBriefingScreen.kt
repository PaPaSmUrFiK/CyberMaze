package com.cybermaze.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cybermaze.core.game.model.LevelConfig
import com.cybermaze.core.ui.theme.CyberColors
import com.cybermaze.core.ui.theme.LevelPalette

/**
 * Pre-game overlay that explicitly explains how stars are awarded for the
 * current level before the player makes a move. Triggered when
 * [com.cybermaze.core.game.model.GamePhase.BRIEFING] is the active phase.
 */
@Composable
fun LevelBriefingScreen(
    config: LevelConfig,
    onStart: () -> Unit,
    modifier: Modifier = Modifier,
    palette: LevelPalette = LevelPalette.Default
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CyberColors.OverlayBackground)
            // Block underlying swipe / tap gestures so the briefing acts as a real modal.
            .pointerInput(Unit) {},
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 420.dp)
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .background(CyberColors.SurfaceDark, RoundedCornerShape(16.dp))
                .border(2.dp, palette.hudTitle, RoundedCornerShape(16.dp))
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "L${config.levelNumber} · ${config.title.uppercase()}",
                color = palette.hudTitle,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = config.description,
                color = CyberColors.TextPrimary,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            SectionTitle(text = "OBJECTIVES", color = palette.hudScore)
            BulletLine(
                "Collect ${config.requiredPoints} of ${config.totalPoints} energy points"
            )
            BulletLine("Reach the exit (E)")

            Spacer(modifier = Modifier.height(6.dp))

            SectionTitle(text = "HOW TO EARN STARS", color = palette.hudScore)
            StarLine(
                star = "1",
                text = "Complete the level",
                color = palette.hudTitle
            )
            StarLine(
                star = "2",
                text = config.timeLimit?.let {
                    val target = (it * 0.75f).toInt()
                    "Finish in under ${target}s (time limit ${it}s)"
                } ?: "Speed bonus — not available on this level",
                color = palette.hudTitle,
                dimmed = config.timeLimit == null
            )
            StarLine(
                star = "3",
                text = "Perfect run — collect all ${config.totalPoints} energy and lose no lives",
                color = palette.hudTitle
            )

            Spacer(modifier = Modifier.height(8.dp))

            NeonButton(
                text = "START",
                onClick = onStart,
                color = palette.hudTitle
            )
        }
    }
}

@Composable
private fun SectionTitle(text: String, color: Color) {
    Text(
        text = text,
        color = color,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun BulletLine(text: String) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = "•", color = CyberColors.TextSecondary, fontSize = 14.sp)
        Text(
            text = text,
            color = CyberColors.TextPrimary,
            fontSize = 13.sp
        )
    }
}

@Composable
private fun StarLine(
    star: String,
    text: String,
    color: Color,
    dimmed: Boolean = false
) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "★$star",
            color = if (dimmed) color.copy(alpha = 0.4f) else color,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = text,
            color = if (dimmed) CyberColors.TextSecondary.copy(alpha = 0.6f) else CyberColors.TextPrimary,
            fontSize = 13.sp
        )
    }
}
