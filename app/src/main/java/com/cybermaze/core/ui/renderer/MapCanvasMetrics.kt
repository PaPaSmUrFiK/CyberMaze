package com.cybermaze.core.ui.renderer

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.cybermaze.core.game.model.GameMap
import com.cybermaze.core.game.model.Position

/**
 * Shared map→pixel mapping so [GameCanvas] and optional overlay layers align exactly.
 */
data class MapCanvasMetrics(
    val tileSize: Float,
    val offsetX: Float,
    val offsetY: Float,
    val mapWidth: Int,
    val mapHeight: Int
) {
    fun tileTopLeft(pos: Position): Offset =
        Offset(pos.x * tileSize + offsetX, pos.y * tileSize + offsetY)

    fun tileCenter(pos: Position): Offset =
        Offset(
            pos.x * tileSize + tileSize / 2f + offsetX,
            pos.y * tileSize + tileSize / 2f + offsetY
        )
}

fun Size.toMapMetrics(map: GameMap): MapCanvasMetrics {
    val w = map.width
    val h = map.height
    val tileSize = kotlin.math.min(this.width / w, this.height / h)
    val offsetX = (this.width - w * tileSize) / 2f
    val offsetY = (this.height - h * tileSize) / 2f
    return MapCanvasMetrics(tileSize, offsetX, offsetY, w, h)
}
