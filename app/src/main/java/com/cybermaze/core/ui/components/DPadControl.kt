package com.cybermaze.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cybermaze.core.game.model.Direction
import com.cybermaze.core.ui.theme.CyberColors

/**
 * Translucent D-pad overlay used as the secondary input alongside swipes.
 * Each tap changes the player's intended direction; the actual movement is
 * driven by the game loop, so a single tap is enough to keep moving.
 */
@Composable
fun DPadControl(
    onDirection: (Direction) -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonSize = 56.dp
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        DPadButton(label = "▲", size = buttonSize) { onDirection(Direction.UP) }
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            DPadButton(label = "◀", size = buttonSize) { onDirection(Direction.LEFT) }
            Spacer(modifier = Modifier.size(buttonSize))
            DPadButton(label = "▶", size = buttonSize) { onDirection(Direction.RIGHT) }
        }
        DPadButton(label = "▼", size = buttonSize) { onDirection(Direction.DOWN) }
    }
}

@Composable
private fun DPadButton(
    label: String,
    size: Dp,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(10.dp))
            .background(CyberColors.ButtonBackground)
            .border(2.dp, CyberColors.ButtonBorder.copy(alpha = 0.9f), RoundedCornerShape(10.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = label, color = CyberColors.NeonCyan, fontSize = 22.sp)
    }
}
