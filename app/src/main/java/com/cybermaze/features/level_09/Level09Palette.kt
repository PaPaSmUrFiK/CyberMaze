package com.cybermaze.features.level_09

import androidx.compose.ui.graphics.Color
import com.cybermaze.core.ui.theme.LevelPalette

/** Default visual mode for Level 9: cyan + strong neon purple accents. */
val LEVEL_09_PALETTE_NORMAL: LevelPalette = LevelPalette(
    background = Color(0xFF08091F),
    grid = Color(0xFFA057FF).copy(alpha = 0.10f),
    wallFill = Color(0xFF1D2668),
    wallStroke = Color(0xFFB06EFF).copy(alpha = 0.36f),
    player = Color(0xFF55FFD1),
    enemyChaser = Color(0xFFFF4E72),
    enemyPatrol = Color(0xFFFFB14E),
    enemyRandom = Color(0xFFFF8A3A),
    energyPoint = Color(0xFF7EE5FF),
    key = Color(0xFFFFD55D),
    shield = Color(0xFFC68BFF),
    teleportA = Color(0xFF9D4BFF),
    teleportB = Color(0xFFE14CFF),
    door = Color(0xFFB56A2A),
    hudTitle = Color(0xFFC58BFF),
    hudScore = Color(0xFF89E4FF),
    hudProgress = Color(0xFF89E4FF),
    hudPauseIcon = Color(0xFFC58BFF)
)

/** Frenzy visual mode: red-neon tension while enemies are accelerated. */
val LEVEL_09_PALETTE_FRENZY: LevelPalette = LevelPalette(
    background = Color(0xFF1B0712),
    grid = Color(0xFFFF3B54).copy(alpha = 0.12f),
    wallFill = Color(0xFF5B1730),
    wallStroke = Color(0xFFFF628A).copy(alpha = 0.38f),
    player = Color(0xFFC6A0FF),
    enemyChaser = Color(0xFFFF3047),
    enemyPatrol = Color(0xFFFF8C3A),
    enemyRandom = Color(0xFFFF6A2C),
    energyPoint = Color(0xFFFF7D98),
    key = Color(0xFFFFD55D),
    shield = Color(0xFFFFA9C1),
    teleportA = Color(0xFFFF5CF2),
    teleportB = Color(0xFFFF7A43),
    door = Color(0xFFC35F2A),
    hudTitle = Color(0xFFFF79A9),
    hudScore = Color(0xFFFFAEC6),
    hudProgress = Color(0xFFFFAEC6),
    hudPauseIcon = Color(0xFFFF79A9),
    hudTimerOk = Color(0xFFFF8494),
    hudTimerWarn = Color(0xFFFF6B7E),
    hudTimerCritical = Color(0xFFFF3047)
)
