package com.cybermaze.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cybermaze.core.ui.theme.LevelPalette

/**
 * Heads-up display drawn across the top of the GameScreen.
 *
 * Layout (landscape): [lives] | [level title + score] | [progress / timer / keys / shield] | [pause]
 *
 * All colors come from [palette]; omit it for the default Cyber Maze look.
 */
@Composable
fun GameHUD(
    levelTitle: String,
    levelNumber: Int,
    lives: Int,
    score: Int,
    collectedPoints: Int,
    totalPoints: Int,
    timeRemaining: Int? = null,
    keysCount: Int = 0,
    hasShield: Boolean = false,
    powerUpSecondsLeft: Int? = null,
    speedBoostSecondsLeft: Int? = null,
    palette: LevelPalette = LevelPalette.Default,
    onPause: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(palette.hudBackground)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(lives.coerceAtMost(3)) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Life",
                    tint = palette.hudLife,
                    modifier = Modifier.size(18.dp)
                )
            }
            if (lives <= 0) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "No lives",
                    tint = palette.hudLifeEmpty.copy(alpha = 0.3f),
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = "L$levelNumber ${levelTitle.uppercase()}",
            color = palette.hudTitle,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "SCORE $score",
            color = palette.hudScore,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "$collectedPoints/$totalPoints",
            color = palette.hudProgress,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        if (hasShield) {
            HudBadge(
                icon = Icons.Default.Shield,
                tint = palette.shield
            )
        }

        if ((powerUpSecondsLeft ?: 0) > 0) {
            HudBadge(
                icon = Icons.Default.FlashOn,
                tint = palette.powerUp,
                label = "${powerUpSecondsLeft}s"
            )
        }

        if ((speedBoostSecondsLeft ?: 0) > 0) {
            HudBadge(
                icon = Icons.Default.Bolt,
                tint = palette.speedBoost,
                label = "${speedBoostSecondsLeft}s"
            )
        }

        if (keysCount > 0) {
            HudBadge(
                icon = Icons.Default.VpnKey,
                tint = palette.key,
                label = "$keysCount"
            )
        }

        timeRemaining?.let { time ->
            val timerColor: Color = when {
                time > 30 -> palette.hudTimerOk
                time > 10 -> palette.hudTimerWarn
                else -> palette.hudTimerCritical
            }
            Text(
                text = formatTime(time),
                color = timerColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        IconButton(
            onClick = onPause,
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Pause,
                contentDescription = "Pause",
                tint = palette.hudPauseIcon
            )
        }
    }
}

@Composable
private fun RowScope.HudBadge(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    label: String? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier.padding(PaddingValues(horizontal = 2.dp))
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(16.dp)
        )
        if (label != null) {
            Text(text = label, color = tint, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
    }
}

private fun formatTime(seconds: Int): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return "%02d:%02d".format(mins, secs)
}
