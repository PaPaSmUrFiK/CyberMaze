package com.cybermaze.features.level_10

import androidx.compose.ui.graphics.Color
import com.cybermaze.core.ui.theme.LevelPalette

/**
 * "Final Core" palette — saturated crimson and amber on near-black, evoking a
 * collapsing system reactor. Pairs with the glitch [VictoryAnimation] played on
 * level completion.
 */
val LEVEL_10_PALETTE: LevelPalette = LevelPalette(
    background = Color(0xFF080004),
    grid = Color(0xFFFF1744).copy(alpha = 0.08f),
    wallFill = Color(0xFF3A0010),
    wallStroke = Color(0xFFFF1744).copy(alpha = 0.45f),

    player = Color(0xFFFFEB3B),
    enemyChaser = Color(0xFFFF1744),
    enemyPatrol = Color(0xFFFF6F00),
    enemyFast = Color(0xFFFF00AA),
    enemyRandom = Color(0xFFFF5722),
    enemyGuard = Color(0xFFFFAB00),

    energyPoint = Color(0xFFFFC400),
    key = Color(0xFFFFE082),
    powerUp = Color(0xFFE040FB),
    speedBoost = Color(0xFFFFEA00),
    shield = Color(0xFF00E5FF),
    bonusStar = Color(0xFFFFD740),

    exit = Color(0xFF76FF03),
    door = Color(0xFFFF6D00),
    teleportA = Color(0xFFFF1744),
    teleportB = Color(0xFFFFAB00),
    trap = Color(0xFFFF1744),
    guardVision = Color(0xFFFFAB00),

    fogColor = Color(0xFF000000),
    fogDarkness = 0.78f,

    hudBackground = Color(0xCC150006),
    hudTitle = Color(0xFFFF1744),
    hudScore = Color(0xFFFFAB00),
    hudProgress = Color(0xFFFFEB3B),
    hudPauseIcon = Color(0xFFFFAB00),
    hudLife = Color(0xFFFF1744)
)
