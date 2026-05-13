package com.cybermaze.features.level_template

import androidx.compose.ui.graphics.Color
import com.cybermaze.core.ui.theme.LevelPalette

/**
 * Example "reskin" palette for the template level — a warm "furnace" style that
 * deliberately diverges from the default cool cyber-cyan look.
 *
 * To use the default look, simply omit `palette = …` when building your
 * [com.cybermaze.core.game.engine.LevelDefinition] — it will fall back to
 * [LevelPalette.Default].
 *
 * Tips:
 *   - You only need to override the fields you want to change. Every field has
 *     a sane default mirroring the original Cyber Maze palette.
 *   - Keep `wallFill` dark enough to stay readable against [background].
 *   - For accessibility, ensure `player` contrasts strongly with both [background]
 *     and [wallFill].
 */
val LEVEL_TEMPLATE_PALETTE: LevelPalette = LevelPalette(
    background = Color(0xFF1A0500),
    grid = Color(0xFFFF6A00).copy(alpha = 0.10f),
    wallFill = Color(0xFF5A1500),
    wallStroke = Color(0xFFFF8A00).copy(alpha = 0.35f),

    player = Color(0xFFFFE08A),
    enemyChaser = Color(0xFFFF3D00),
    enemyPatrol = Color(0xFFFFB347),
    enemyFast = Color(0xFFFF0066),
    enemyGuard = Color(0xFFFFAA00),

    energyPoint = Color(0xFFFFC107),
    key = Color(0xFFFFE066),
    powerUp = Color(0xFFFF66CC),
    speedBoost = Color(0xFFFFEB3B),

    exit = Color(0xFFFFE08A),
    door = Color(0xFFFFA000),
    teleportA = Color(0xFFFF66AA),
    teleportB = Color(0xFFFFD54F),
    trap = Color(0xFFFF1744),
    guardVision = Color(0xFFFFAA00),

    fogColor = Color(0xFF1A0500),

    hudBackground = Color(0xAA1A0500),
    hudTitle = Color(0xFFFFB347),
    hudScore = Color(0xFFFFE08A),
    hudProgress = Color(0xFFFFE08A),
    hudPauseIcon = Color(0xFFFFAA00),
    hudLife = Color(0xFFFF3D00)
)
