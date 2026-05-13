package com.cybermaze.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
 * Overlay shown when the player loses (no lives / timeout).
 */
@Composable
fun GameOverScreen(
    score: Int,
    reason: String? = null,
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
                .width(320.dp)
                .background(CyberColors.SurfaceDark, RoundedCornerShape(16.dp))
                .border(2.dp, CyberColors.EnemyChaser, RoundedCornerShape(16.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "SYSTEM FAILURE",
                color = CyberColors.EnemyChaser,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            reason?.let {
                Text(
                    text = it.uppercase(),
                    color = CyberColors.TextSecondary,
                    fontSize = 12.sp
                )
            }
            Text(
                text = "SCORE $score",
                color = CyberColors.TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            NeonButton(text = "RETRY", onClick = onRestart)
            NeonButton(text = "EXIT", onClick = onExit, color = CyberColors.TextSecondary)
        }
    }
}
