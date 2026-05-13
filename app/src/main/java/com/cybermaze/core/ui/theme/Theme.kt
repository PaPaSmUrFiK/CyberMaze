package com.cybermaze.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CyberColorScheme = darkColorScheme(
    primary = CyberColors.NeonCyan,
    secondary = CyberColors.NeonGreen,
    tertiary = CyberColors.NeonPink,
    background = CyberColors.Background,
    surface = CyberColors.SurfaceDark,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = CyberColors.TextPrimary,
    onSurface = CyberColors.TextPrimary
)

/**
 * Main theme for Cyber Maze.
 * Applies cyberpunk color scheme and typography.
 */
@Composable
fun CyberMazeTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CyberColorScheme,
        typography = CyberTypography,
        content = content
    )
}
