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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cybermaze.core.ui.theme.CyberColors

/**
 * Overlay shown when the player beats a level.
 *
 * Star conditions are displayed explicitly so the player understands which
 * specific star they earned or missed and how to retry for a 3-star run.
 */
@Composable
fun LevelCompleteScreen(
    levelNumber: Int,
    levelTitle: String,
    score: Int,
    timeSeconds: Float,
    stars: Int,
    hasNextLevel: Boolean,
    onNextLevel: () -> Unit,
    onRestart: () -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier,
    timeLimit: Int? = null,
    totalEnergy: Int = 0,
    collectedEnergy: Int = 0,
    livesLost: Int = 0
) {
    val completedStar = true
    val speedStar = timeLimit != null && timeSeconds <= timeLimit * 0.75f
    val perfectStar = totalEnergy > 0 && collectedEnergy >= totalEnergy && livesLost == 0

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CyberColors.OverlayBackground)
            .pointerInput(Unit) {},
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 420.dp)
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .background(CyberColors.SurfaceDark, RoundedCornerShape(16.dp))
                .border(2.dp, CyberColors.NeonGreen, RoundedCornerShape(16.dp))
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "LEVEL COMPLETE",
                color = CyberColors.NeonGreen,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "L$levelNumber · ${levelTitle.uppercase()}",
                color = CyberColors.TextPrimary,
                fontSize = 14.sp
            )

            StarsRow(stars = stars)

            Text(
                text = "SCORE $score",
                color = CyberColors.NeonCyan,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "TIME ${formatTime(timeSeconds)}",
                color = CyberColors.TextSecondary,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            ConditionLine(
                earned = completedStar,
                label = "Complete the level"
            )
            ConditionLine(
                earned = speedStar,
                disabled = timeLimit == null,
                label = timeLimit?.let {
                    val target = (it * 0.75f).toInt()
                    "Finish under ${target}s (you: ${timeSeconds.toInt()}s)"
                } ?: "Speed bonus — not available on this level"
            )
            ConditionLine(
                earned = perfectStar,
                label = perfectLabel(
                    collectedEnergy = collectedEnergy,
                    totalEnergy = totalEnergy,
                    livesLost = livesLost,
                    earned = perfectStar
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (hasNextLevel) {
                NeonButton(text = "NEXT LEVEL", onClick = onNextLevel, color = CyberColors.NeonGreen)
            } else {
                NeonButton(text = "FINISH", onClick = onExit, color = CyberColors.NeonGreen)
            }
            NeonButton(text = "RETRY", onClick = onRestart)
            NeonButton(text = "EXIT", onClick = onExit, color = CyberColors.TextSecondary)
        }
    }
}

private fun perfectLabel(
    collectedEnergy: Int,
    totalEnergy: Int,
    livesLost: Int,
    earned: Boolean
): String {
    if (earned) return "Perfect run — all $totalEnergy energy and no lives lost"
    val missingEnergy = (totalEnergy - collectedEnergy).coerceAtLeast(0)
    val parts = mutableListOf<String>()
    if (missingEnergy > 0) parts += "missed $missingEnergy energy"
    if (livesLost > 0) parts += "died $livesLost time" + if (livesLost > 1) "s" else ""
    val why = if (parts.isEmpty()) "" else " (${parts.joinToString(", ")})"
    return "Perfect run — collect all $totalEnergy and don't die$why"
}

@Composable
private fun ConditionLine(
    earned: Boolean,
    label: String,
    disabled: Boolean = false
) {
    val mark = when {
        disabled -> "—"
        earned -> "✓"
        else -> "✗"
    }
    val markColor = when {
        disabled -> CyberColors.TextSecondary.copy(alpha = 0.5f)
        earned -> CyberColors.NeonGreen
        else -> CyberColors.EnemyChaser
    }
    val textColor = when {
        disabled -> CyberColors.TextSecondary.copy(alpha = 0.6f)
        earned -> CyberColors.TextPrimary
        else -> CyberColors.TextSecondary
    }
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = mark,
            color = markColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Text(text = label, color = textColor, fontSize = 13.sp)
    }
}

@Composable
private fun StarsRow(stars: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        repeat(3) { i ->
            val earned = i < stars
            Text(
                text = if (earned) "★" else "☆",
                color = if (earned) CyberColors.NeonYellow else CyberColors.TextSecondary.copy(alpha = 0.4f),
                fontSize = 36.sp
            )
        }
    }
}

private fun formatTime(seconds: Float): String {
    val total = seconds.toInt()
    val mins = total / 60
    val secs = total % 60
    return "%02d:%02d".format(mins, secs)
}
