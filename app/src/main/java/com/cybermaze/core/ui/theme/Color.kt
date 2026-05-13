package com.cybermaze.core.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Cyberpunk color palette for the game.
 * Neon colors on dark backgrounds.
 */
object CyberColors {
    // Backgrounds
    val Background = Color(0xFF050510)
    val SurfaceDark = Color(0xFF0A0A1F)
    val SurfaceMid = Color(0xFF111130)
    
    // Neon colors
    val NeonCyan = Color(0xFF00FFFF)
    val NeonGreen = Color(0xFF00FF88)
    val NeonPink = Color(0xFFFF00AA)
    val NeonPurple = Color(0xFFAA00FF)
    val NeonOrange = Color(0xFFFF6600)
    val NeonYellow = Color(0xFFFFD700)
    val NeonBlue = Color(0xFF00BFFF)
    
    // Game elements
    val PlayerColor = NeonGreen
    val EnemyPatrol = NeonOrange
    val EnemyChaser = Color(0xFFFF3030)
    val EnemyFast = NeonPink
    val EnemyRandom = Color(0xFFFF8C00)
    val WallColor = Color(0xFF1A3A5C)
    val EnergyPoint = NeonBlue
    val ExitColor = NeonGreen
    val DoorColor = NeonOrange
    val KeyColor = NeonYellow
    val TeleportA = NeonPurple
    val TeleportB = NeonPink
    val TrapColor = Color(0xFFFF0000)
    val ShieldColor = NeonCyan
    
    // Text
    val TextPrimary = Color(0xFFE0E8FF)
    val TextSecondary = Color(0xFF8899CC)
    val TextNeon = NeonCyan
    
    // UI elements
    val ButtonBorder = NeonCyan
    val ButtonBackground = Color(0x33001133)
    val HudBackground = Color(0xAA000011)
    val OverlayBackground = Color(0xDD000000)
}
