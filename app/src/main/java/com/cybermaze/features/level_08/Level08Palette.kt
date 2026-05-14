package com.cybermaze.features.level_08

import androidx.compose.ui.graphics.Color
import com.cybermaze.core.ui.theme.LevelPalette

/** Amber hunt / dusk grid — distinct from Data Harvest cyan. */
val LEVEL_08_PALETTE: LevelPalette = LevelPalette(
    background = Color(0xFF120508),
    grid = Color(0xFFFFB703).copy(alpha = 0.06f),
    wallFill = Color(0xFF2A1018),
    wallStroke = Color(0xFFFF8C42).copy(alpha = 0.32f),

    player = Color(0xFF00F5FF),
    enemyChaser = Color(0xFFFF0055),
    enemyPatrol = Color(0xFFFFB703),
    enemyFast = Color(0xFFFF3D00),
    enemyGuard = Color(0xFFE040FB),

    energyPoint = Color(0xFFFFE066),
    shield = Color(0xFF66D9FF),
    guardVision = Color(0xFFE040FB),

    exit = Color(0xFF00FFAA),
    hudBackground = Color(0xCC180810),
    hudTitle = Color(0xFFFFB703),
    hudScore = Color(0xFFFFE0B2),
    hudLife = Color(0xFFFF0055)
)
