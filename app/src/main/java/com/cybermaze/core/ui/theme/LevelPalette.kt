package com.cybermaze.core.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Per-level visual palette. Every renderer (GameCanvas, optional overlay layers, HUD)
 * reads its colors from this object. Defaults reproduce the original "Cyber Maze" look
 * driven by [CyberColors], so existing levels keep working when [LevelPalette] is omitted.
 *
 * Level authors override fields they want to "reskin" — for example, level 03 "Furnace"
 * might set `background = Color(0xFF1A0A00)`, `wall = Color(0xFF6B2200)`,
 * `player = Color(0xFFFFD27F)` and leave everything else as default.
 *
 * Keep all visual constants here — the rest of the codebase MUST NOT hardcode colors
 * inside the renderer Composables.
 */
data class LevelPalette(
    // Map / background
    val background: Color = CyberColors.Background,
    val grid: Color = CyberColors.NeonCyan.copy(alpha = 0.06f),
    val wallFill: Color = CyberColors.WallColor,
    val wallStroke: Color = CyberColors.NeonCyan.copy(alpha = 0.25f),

    // Entities
    val player: Color = CyberColors.PlayerColor,
    val enemyChaser: Color = CyberColors.EnemyChaser,
    val enemyPatrol: Color = CyberColors.EnemyPatrol,
    val enemyFast: Color = CyberColors.EnemyFast,
    val enemyRandom: Color = CyberColors.EnemyRandom,
    val enemyGuard: Color = CyberColors.NeonPurple,

    // Collectibles
    val energyPoint: Color = CyberColors.EnergyPoint,
    val key: Color = CyberColors.KeyColor,
    val powerUp: Color = CyberColors.NeonPurple,
    val speedBoost: Color = CyberColors.NeonYellow,
    val shield: Color = CyberColors.ShieldColor,
    val bonusStar: Color = CyberColors.NeonYellow,

    // Static map decorations
    val exit: Color = CyberColors.ExitColor,
    val door: Color = CyberColors.DoorColor,
    val teleportA: Color = CyberColors.TeleportA,
    val teleportB: Color = CyberColors.TeleportB,
    val trap: Color = CyberColors.TrapColor,

    // Effects
    val guardVision: Color = CyberColors.NeonPurple,
    val fogColor: Color = Color.Black,
    val fogDarkness: Float = 0.88f,

    // HUD
    val hudBackground: Color = CyberColors.HudBackground,
    val hudTitle: Color = CyberColors.NeonGreen,
    val hudScore: Color = CyberColors.TextNeon,
    val hudProgress: Color = CyberColors.NeonGreen,
    val hudPauseIcon: Color = CyberColors.NeonCyan,
    val hudLife: Color = CyberColors.EnemyChaser,
    val hudLifeEmpty: Color = CyberColors.TextSecondary,
    val hudTimerOk: Color = CyberColors.NeonGreen,
    val hudTimerWarn: Color = CyberColors.NeonYellow,
    val hudTimerCritical: Color = CyberColors.EnemyChaser
) {
    companion object {
        /** Default palette — exactly the original Cyber Maze look. */
        val Default: LevelPalette = LevelPalette()
    }
}
