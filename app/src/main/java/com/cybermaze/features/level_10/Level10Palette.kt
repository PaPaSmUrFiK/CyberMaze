package com.cybermaze.features.level_10

import androidx.compose.ui.graphics.Color
import com.cybermaze.core.ui.theme.LevelPalette

/**
 * "Final Core" palette — slightly lighter burgundy / amber so corridors and collectibles
 * stay readable. Pairs with [VictoryAnimation] on win.
 */
val LEVEL_10_PALETTE: LevelPalette = LevelPalette(
    background = Color(0xFF1A0C14),
    grid = Color(0xFFFF8A80).copy(alpha = 0.14f),
    wallFill = Color(0xFF4A2030),
    wallStroke = Color(0xFFFF8A80).copy(alpha = 0.55f),

    player = Color(0xFFFFF59D),
    enemyChaser = Color(0xFFFF5252),
    enemyPatrol = Color(0xFFFFAB40),
    enemyFast = Color(0xFFFF4081),
    enemyRandom = Color(0xFFFF7043),
    enemyGuard = Color(0xFFFFD54F),

    energyPoint = Color(0xFFFFE082),
    key = Color(0xFF80DEEA),
    powerUp = Color(0xFFEA80FC),
    speedBoost = Color(0xFFFFF176),
    shield = Color(0xFF84FFFF),
    bonusStar = Color(0xFFFFE57F),

    exit = Color(0xFFB9F6CA),
    door = Color(0xFFFFB74D),
    teleportA = Color(0xFFFF5252),
    teleportB = Color(0xFFFFD54F),
    trap = Color(0xFFFF5252),
    guardVision = Color(0xFFFFD54F).copy(alpha = 0.85f),

    fogColor = Color(0xFF000000),
    fogDarkness = 0.55f,

    hudBackground = Color(0xD9261218),
    hudTitle = Color(0xFFFF8A80),
    hudScore = Color(0xFFFFE082),
    hudProgress = Color(0xFFFFF9C4),
    hudPauseIcon = Color(0xFFFFCC80),
    hudLife = Color(0xFFFF8A80)
)
