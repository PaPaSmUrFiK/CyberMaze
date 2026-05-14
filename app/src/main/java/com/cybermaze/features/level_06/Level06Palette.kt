package com.cybermaze.features.level_06

import androidx.compose.ui.graphics.Color
import com.cybermaze.core.ui.theme.LevelPalette

/**
 * Custom palette for Level 06 "Warp Zone".
 * Uses purple/pink theme to emphasize the teleportation theme.
 */
val LEVEL_06_PALETTE = LevelPalette(
    background = Color(0xFF0F0028),
    wallFill = Color(0xFF2D004B),
    wallStroke = Color(0xFF7B00FF),
    energyPoint = Color(0xFFE5FF00),
    player = Color(0xFF00FFFF),
    enemyChaser = Color(0xFFFF0066),
    enemyPatrol = Color(0xFFFF8C00),
    hudTitle = Color(0xFFAA00FF)
)
