package com.cybermaze.features.level_02

import androidx.compose.ui.graphics.Color
import com.cybermaze.core.ui.theme.LevelPalette

/** Cool cyan / magenta “data stream” look for Data Harvest. */
val LEVEL_02_PALETTE: LevelPalette = LevelPalette(
    background = Color(0xFF020814),
    grid = Color(0xFF00E5FF).copy(alpha = 0.07f),
    wallFill = Color(0xFF0A1A2E),
    wallStroke = Color(0xFF00B4D8).copy(alpha = 0.35f),

    player = Color(0xFF7CFFC4),
    enemyChaser = Color(0xFFFF4D8D),
    enemyPatrol = Color(0xFFFFA94D),
    enemyRandom = Color(0xFFB388FF),

    energyPoint = Color(0xFF00F5D4),
    powerUp = Color(0xFFFF00E5),

    exit = Color(0xFF00FFF0),
    hudBackground = Color(0xCC051018),
    hudTitle = Color(0xFF00E5FF),
    hudScore = Color(0xFFB8ECFF),
    hudProgress = Color(0xFF00F5A0),
    hudTimerOk = Color(0xFF00F5A0)
)
