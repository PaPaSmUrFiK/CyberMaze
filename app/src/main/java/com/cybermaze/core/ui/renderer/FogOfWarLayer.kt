package com.cybermaze.core.ui.renderer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import com.cybermaze.core.game.model.GameState

/**
 * Dark overlay with a circular "hole" around the player (fog of war).
 *
 * Color and darkness come from [GameState.palette] (`fogColor` + `fogDarkness`).
 * Uses [BlendMode.DstOut] to punch through the darkness so the underlying
 * [GameCanvas] remains visible inside the vision radius.
 */
@Composable
fun FogOfWarLayer(
    gameState: GameState,
    modifier: Modifier = Modifier
) {
    val radiusTiles = gameState.effectiveFogRadiusTiles() ?: return
    val palette = gameState.palette

    Canvas(modifier = modifier.fillMaxSize()) {
        val m = size.toMapMetrics(gameState.map)
        val center = m.tileCenter(gameState.player.position)
        val radiusPx = radiusTiles * m.tileSize * 0.95f

        drawRect(color = palette.fogColor.copy(alpha = palette.fogDarkness.coerceIn(0f, 1f)))
        drawCircle(
            color = Color.White,
            radius = radiusPx,
            center = center,
            blendMode = BlendMode.DstOut
        )
    }
}
