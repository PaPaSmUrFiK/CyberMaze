package com.cybermaze.core.ui.components

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cybermaze.core.game.model.Direction
import kotlin.math.abs

/**
 * Translates touch drags into directional inputs.
 *
 * The pointer's accumulated travel is tracked across the gesture. As soon as
 * the absolute travel along the dominant axis exceeds [threshold], we report
 * a direction AND reset the accumulator on that axis so the player can keep
 * "steering" without lifting the finger.
 */
fun Modifier.swipeDirections(
    onDirection: (Direction) -> Unit,
    threshold: Dp = 24.dp
): Modifier = composed {
    val thresholdPx = with(LocalDensity.current) { threshold.toPx() }
    pointerInput(Unit) {
        var accumulated = Offset.Zero
        detectDragGestures(
            onDragStart = { accumulated = Offset.Zero },
            onDragEnd = { accumulated = Offset.Zero },
            onDragCancel = { accumulated = Offset.Zero }
        ) { _, dragAmount ->
            accumulated += dragAmount
            val dx = accumulated.x
            val dy = accumulated.y
            if (abs(dx) >= thresholdPx || abs(dy) >= thresholdPx) {
                if (abs(dx) > abs(dy)) {
                    onDirection(if (dx > 0) Direction.RIGHT else Direction.LEFT)
                    accumulated = Offset(0f, dy)
                } else {
                    onDirection(if (dy > 0) Direction.DOWN else Direction.UP)
                    accumulated = Offset(dx, 0f)
                }
            }
        }
    }
}
