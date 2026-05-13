package com.cybermaze.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cybermaze.core.ui.theme.CyberColors

/**
 * Overlay shown when the player pauses the game.
 */
@Composable
fun PauseMenu(
    onResume: () -> Unit,
    onRestart: () -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CyberColors.OverlayBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .width(280.dp)
                .background(CyberColors.SurfaceDark, RoundedCornerShape(16.dp))
                .border(2.dp, CyberColors.NeonCyan, RoundedCornerShape(16.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "PAUSED",
                color = CyberColors.NeonCyan,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            NeonButton(text = "RESUME", onClick = onResume, color = CyberColors.NeonGreen)
            NeonButton(text = "RESTART", onClick = onRestart)
            NeonButton(text = "EXIT", onClick = onExit, color = CyberColors.EnemyChaser)
        }
    }
}
