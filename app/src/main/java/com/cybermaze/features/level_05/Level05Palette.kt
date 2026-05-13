package com.cybermaze.features.level_05

import androidx.compose.ui.graphics.Color
import com.cybermaze.core.ui.theme.LevelPalette

/**
 * Danger-themed palette for Trap Matrix — red/orange alerts.
 */
val LEVEL_05_PALETTE: LevelPalette = LevelPalette(
    background = Color(0xFF0A0000),
    grid = Color(0xFFFF0000).copy(alpha = 0.08f),
    wallFill = Color(0xFF2A0505),
    wallStroke = Color(0xFFFF3333).copy(alpha = 0.35f),

    player = Color(0xFF00FF88),  // Keep green for contrast
    enemyChaser = Color(0xFFFF4444),
    enemyPatrol = Color(0xFFFF8844),

    energyPoint = Color(0xFFFF6666),
    trap = Color(0xFFFF0000),

    hudBackground = Color(0xCC1A0000),
    hudTitle = Color(0xFFFF4444),
    hudScore = Color(0xFFFF8888),
    hudLife = Color(0xFFFF0000)
)