package com.cybermaze.features.level_07

import androidx.compose.ui.graphics.Color
import com.cybermaze.core.ui.theme.LevelPalette

/** Pastel pink / lavender “playful” theme — full map visibility (no fog overlay). */
val LEVEL_07_PALETTE: LevelPalette = LevelPalette(
    background = Color(0xFFFFF5FC),
    grid = Color(0xFFF48FB1).copy(alpha = 0.22f),
    wallFill = Color(0xFFF8BBD0),
    wallStroke = Color(0xFFE91E8C).copy(alpha = 0.45f),

    player = Color(0xFF7C4DFF),
    enemyChaser = Color(0xFFFF4081),
    enemyPatrol = Color(0xFFFF80AB),
    enemyFast = Color(0xFFFF5252),
    enemyGuard = Color(0xFFD500F9),

    energyPoint = Color(0xFFFFC1E3),
    key = Color(0xFFFFE082),
    powerUp = Color(0xFFEA80FC),
    speedBoost = Color(0xFFFFEA00),
    shield = Color(0xFF80D8FF),
    bonusStar = Color(0xFFFFD740),

    exit = Color(0xFF69F0AE),
    door = Color(0xFFFFAB40),
    teleportA = Color(0xFFFF4081),
    teleportB = Color(0xFFFF80AB),
    trap = Color(0xFFFF4081),
    guardVision = Color(0xFFE040FB).copy(alpha = 0.55f),

    fogColor = Color(0xFF000000),
    fogDarkness = 0.35f,

    hudBackground = Color(0xE6FCE4EC),
    hudTitle = Color(0xFFE91E8C),
    hudScore = Color(0xFFAD1457),
    hudProgress = Color(0xFF880E4F),
    hudPauseIcon = Color(0xFFE91E8C),
    hudLife = Color(0xFFFF4081)
)
