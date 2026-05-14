package com.cybermaze.features.level_03

import androidx.compose.ui.graphics.Color
import com.cybermaze.core.ui.theme.LevelPalette

val LEVEL_03_PALETTE = LevelPalette(

    // Background
    background = Color(0xFF050816),
    grid = Color(0xFF00FFFF).copy(alpha = 0.08f),

    // Walls
    wallFill = Color(0xFF111827),
    wallStroke = Color(0xFF00E5FF).copy(alpha = 0.35f),

    // Player
    player = Color(0xFFFFFFFF),

    // Enemies
    enemyChaser = Color(0xFFFF1744),
    enemyPatrol = Color(0xFFFF9100),
    enemyFast = Color(0xFFFF0033),
    enemyGuard = Color(0xFFFFEA00),

    // Collectibles
    energyPoint = Color(0xFF00E5FF),
    key = Color(0xFFFFF176),
    powerUp = Color(0xFFEA80FC),
    speedBoost = Color(0xFF76FF03),

    // Exit
    exit = Color(0xFF00FFAA),

    // Objects
    door = Color(0xFFFF9100),
    teleportA = Color(0xFF00E5FF),
    teleportB = Color(0xFFFF00FF),
    trap = Color(0xFFFF1744),

    // Effects
    guardVision = Color(0x44FF0000),
    fogColor = Color(0xCC000000),

    // HUD
    hudBackground = Color(0xAA000000),
    hudTitle = Color(0xFF00E5FF),
    hudScore = Color(0xFFFFFFFF),
    hudProgress = Color(0xFF76FF03),
    hudPauseIcon = Color(0xFFFFEA00),
    hudLife = Color(0xFFFF1744)
)