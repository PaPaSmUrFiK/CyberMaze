package com.cybermaze.core.game.system

import com.cybermaze.core.game.engine.GameEventEmitter
import com.cybermaze.core.game.engine.LevelSystem
import com.cybermaze.core.game.model.CollectibleType
import com.cybermaze.core.game.model.GameMap
import com.cybermaze.core.game.model.GameState
import com.cybermaze.core.game.model.Position

/**
 * Tracks temporary vision expansion after collecting [CollectibleType.POWER_UP] on fog levels
 * (TZ level 7: radius 3 → 5 for 5 seconds).
 *
 * Rendering uses [GameState.effectiveFogRadiusTiles]; this system only maintains timers.
 */
object FogOfWarSystem : LevelSystem {

    const val VISION_BOOST_DURATION: Float = 5f

    override fun update(state: GameState, deltaTime: Float, emit: GameEventEmitter): GameState {
        if (state.fogVisionBoostSecondsLeft <= 0f) return state
        val left = (state.fogVisionBoostSecondsLeft - deltaTime).coerceAtLeast(0f)
        return state.copy(fogVisionBoostSecondsLeft = left)
    }

    override fun onCollectibleCollected(
        state: GameState,
        type: CollectibleType,
        position: Position,
        emit: GameEventEmitter
    ): GameState {
        if (type != CollectibleType.POWER_UP) return state
        if (state.fogBaseRadiusTiles == null) return state
        return state.copy(fogVisionBoostSecondsLeft = VISION_BOOST_DURATION)
    }

    /** Positions visible from [center] within [radiusTiles] (Manhattan circle / diamond). */
    fun visiblePositions(center: Position, radiusTiles: Int, map: GameMap): Set<Position> {
        val out = mutableSetOf<Position>()
        for (dy in -radiusTiles..radiusTiles) {
            for (dx in -radiusTiles..radiusTiles) {
                if (kotlin.math.abs(dx) + kotlin.math.abs(dy) > radiusTiles) continue
                val p = Position(center.x + dx, center.y + dy)
                if (map.isInBounds(p)) out.add(p)
            }
        }
        return out
    }
}
