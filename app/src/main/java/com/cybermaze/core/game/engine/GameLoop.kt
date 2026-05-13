package com.cybermaze.core.game.engine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * Fixed-step game loop that drives an `onUpdate` callback at ~60 FPS.
 *
 * The loop is owned by a coroutine launched on the caller's [CoroutineScope]
 * (typically `viewModelScope`). [stop] cancels the coroutine so the loop
 * doesn't outlive the ViewModel.
 *
 * Pause/resume zero out the accumulated delta so resuming doesn't produce a
 * massive jump on the first tick.
 */
class GameLoop(
    private val onUpdate: (deltaTime: Float) -> Unit
) {
    @Volatile private var isRunning: Boolean = false
    private var lastFrameNanos: Long = 0L
    private var job: Job? = null

    fun launchIn(scope: CoroutineScope) {
        stop()
        isRunning = true
        lastFrameNanos = System.nanoTime()
        job = scope.launch {
            while (isActive) {
                tick()
                delay(FRAME_DELAY_MS)
            }
        }
    }

    fun stop() {
        isRunning = false
        job?.cancel()
        job = null
    }

    fun pause() {
        isRunning = false
    }

    fun resume() {
        lastFrameNanos = System.nanoTime()
        isRunning = true
    }

    fun isRunning(): Boolean = isRunning

    private fun tick() {
        if (!isRunning) {
            lastFrameNanos = System.nanoTime()
            return
        }
        val now = System.nanoTime()
        val deltaSeconds = ((now - lastFrameNanos) / 1_000_000_000f).coerceAtMost(MAX_DELTA)
        lastFrameNanos = now
        onUpdate(deltaSeconds)
    }

    companion object {
        /** Target frame interval in ms (~60 FPS). */
        const val FRAME_DELAY_MS: Long = 16L

        /** Hard cap on delta to keep simulation stable after long pauses. */
        const val MAX_DELTA: Float = 0.1f
    }
}
