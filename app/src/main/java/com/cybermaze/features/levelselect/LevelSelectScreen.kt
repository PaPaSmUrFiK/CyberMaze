package com.cybermaze.features.levelselect

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cybermaze.core.ui.components.NeonButton
import com.cybermaze.core.ui.theme.CyberColors

/**
 * Level selection screen.
 * Shows all 10 levels with lock/unlock status.
 */
@Composable
fun LevelSelectScreen(
    viewModel: LevelSelectViewModel = hiltViewModel(),
    onLevelClick: (Int) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val levels by viewModel.levels.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberColors.Background)
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "SELECT LEVEL",
                color = CyberColors.NeonCyan,
                fontSize = 32.sp
            )
            
            NeonButton(
                text = "BACK",
                onClick = onBackClick,
                modifier = Modifier.width(120.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Level grid (2 columns x 5 rows)
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(levels) { level ->
                LevelCard(
                    level = level,
                    onClick = { if (level.isUnlocked) onLevelClick(level.levelNumber) }
                )
            }
        }
    }
}

/**
 * Single level card.
 */
@Composable
private fun LevelCard(
    level: LevelInfo,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1.5f)
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (level.isUnlocked) CyberColors.SurfaceDark
                else CyberColors.SurfaceDark.copy(alpha = 0.3f)
            )
            .border(
                width = 2.dp,
                color = if (level.isUnlocked) CyberColors.NeonCyan else CyberColors.TextSecondary.copy(alpha = 0.3f),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(enabled = level.isUnlocked) { onClick() }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (level.isUnlocked) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "LEVEL ${level.levelNumber}",
                    color = CyberColors.NeonCyan,
                    fontSize = 20.sp
                )
                
                Text(
                    text = level.title,
                    color = CyberColors.TextPrimary,
                    fontSize = 14.sp
                )
                
                // Stars
                if (level.starsEarned > 0) {
                    Text(
                        text = "⭐".repeat(level.starsEarned),
                        fontSize = 16.sp
                    )
                }
                
                // Best score
                if (level.bestScore > 0) {
                    Text(
                        text = "Best: ${level.bestScore}",
                        color = CyberColors.TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }
        } else {
            // Locked
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Locked",
                    tint = CyberColors.TextSecondary.copy(alpha = 0.5f),
                    modifier = Modifier.size(32.dp)
                )
                
                Text(
                    text = "LEVEL ${level.levelNumber}",
                    color = CyberColors.TextSecondary.copy(alpha = 0.5f),
                    fontSize = 16.sp
                )
            }
        }
    }
}

/**
 * Level info data class.
 */
data class LevelInfo(
    val levelNumber: Int,
    val title: String,
    val isUnlocked: Boolean = false,
    val isCompleted: Boolean = false,
    val starsEarned: Int = 0,
    val bestScore: Int = 0
)
