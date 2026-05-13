package com.cybermaze.features.level_10

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cybermaze.core.game.model.GamePhase
import com.cybermaze.core.ui.components.DPadControl
import com.cybermaze.core.ui.components.GameHUD
import com.cybermaze.core.ui.components.GameOverScreen
import com.cybermaze.core.ui.components.LevelBriefingScreen
import com.cybermaze.core.ui.components.LevelCompleteScreen
import com.cybermaze.core.ui.components.PauseMenu
import com.cybermaze.core.ui.components.swipeDirections
import com.cybermaze.core.ui.renderer.FogOfWarLayer
import com.cybermaze.core.ui.renderer.GameCanvas
import com.cybermaze.core.ui.renderer.GuardVisionLayer
import com.cybermaze.core.ui.renderer.LevelBackgroundLayer
import com.cybermaze.core.ui.renderer.LevelBackgroundStyle
import com.cybermaze.core.ui.renderer.MovingTrapsLayer
import com.cybermaze.core.ui.renderer.TeleportPulseLayer
import kotlin.math.ceil

/**
 * Boss-level screen — Level 10 "Final Core".
 *
 * Layered (back to front):
 *   1. Custom radial background layer (palette-driven).
 *   2. [GameCanvas] with `drawBackground = false`.
 *   3. Optional overlays: moving traps, teleport pulses, fog of war, guard vision.
 *   4. HUD.
 *   5. D-Pad (only during PLAYING).
 *   6. Phase overlays: briefing, pause, lose.
 *   7. WIN: full-screen [VictoryAnimation] "CORE BREACHED" then the standard
 *      [LevelCompleteScreen] (final level → "FINISH" instead of "NEXT LEVEL").
 */
@Composable
fun Level10Screen(
    viewModel: Level10ViewModel = hiltViewModel(),
    onExit: () -> Unit = {},
    onNextLevel: () -> Unit = {}
) {
    val state = viewModel.gameState.collectAsState().value

    if (state == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LEVEL_10_PALETTE.background),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "LOADING FINAL CORE…", color = LEVEL_10_PALETTE.hudTitle)
        }
        return
    }

    // The glitch animation shows once per WIN transition; tapping it (or letting
    // it finish) reveals the standard LevelCompleteScreen below it.
    var showGlitch by remember { mutableStateOf(true) }
    LaunchedEffect(state.phase) {
        if (state.phase == GamePhase.WIN) showGlitch = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(state.palette.background)
            .swipeDirections(onDirection = viewModel::onDirectionInput)
    ) {
        LevelBackgroundLayer(
            palette = state.palette,
            style = LevelBackgroundStyle.Radial,
            modifier = Modifier.fillMaxSize()
        )

        GameCanvas(
            gameState = state,
            modifier = Modifier.fillMaxSize(),
            drawBackground = false
        )

        MovingTrapsLayer(gameState = state, modifier = Modifier.fillMaxSize())
        TeleportPulseLayer(gameState = state, modifier = Modifier.fillMaxSize())
        if (state.fogBaseRadiusTiles != null) {
            FogOfWarLayer(gameState = state, modifier = Modifier.fillMaxSize())
        }
        GuardVisionLayer(gameState = state, modifier = Modifier.fillMaxSize())

        GameHUD(
            levelTitle = state.config.title,
            levelNumber = state.config.levelNumber,
            lives = state.player.lives,
            score = state.player.score,
            collectedPoints = state.collectedPoints,
            totalPoints = state.config.totalPoints,
            timeRemaining = state.remainingTime(),
            keysCount = state.player.keysCollected,
            hasShield = state.player.hasShield,
            powerUpSecondsLeft = if (state.powerUpSecondsLeft > 0f) {
                ceil(state.powerUpSecondsLeft.toDouble()).toInt()
            } else {
                null
            },
            speedBoostSecondsLeft = if (state.player.speedBoostSecondsLeft > 0f) {
                ceil(state.player.speedBoostSecondsLeft.toDouble()).toInt()
            } else {
                null
            },
            palette = state.palette,
            onPause = viewModel::onPause,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        if (state.phase == GamePhase.PLAYING) {
            DPadControl(
                onDirection = viewModel::onDirectionInput,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 16.dp)
                    .alpha(0.7f)
            )
        }

        when (state.phase) {
            GamePhase.BRIEFING -> LevelBriefingScreen(
                config = state.config,
                onStart = viewModel::onStart,
                palette = state.palette
            )

            GamePhase.PAUSED -> PauseMenu(
                onResume = viewModel::onResume,
                onRestart = viewModel::onRestart,
                onExit = onExit
            )

            GamePhase.WIN -> {
                // Standard summary sits behind the glitch overlay so it's already in
                // place when the player dismisses it.
                LevelCompleteScreen(
                    levelNumber = state.config.levelNumber,
                    levelTitle = state.config.title,
                    score = state.player.score,
                    timeSeconds = state.elapsedTime,
                    stars = viewModel.calculateStars(state),
                    // Level 10 is the last level — no "Next Level" button.
                    hasNextLevel = false,
                    onNextLevel = onNextLevel,
                    onRestart = {
                        showGlitch = true
                        viewModel.onRestart()
                    },
                    onExit = onExit,
                    timeLimit = state.config.timeLimit,
                    totalEnergy = viewModel.totalEnergy(),
                    collectedEnergy = state.collectedPoints,
                    livesLost = state.livesLost
                )
                if (showGlitch) {
                    VictoryAnimation(
                        palette = state.palette,
                        onFinished = { showGlitch = false },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            GamePhase.LOSE -> GameOverScreen(
                score = state.player.score,
                reason = if (!state.player.isAlive()) "out of lives" else "time's up",
                onRestart = viewModel::onRestart,
                onExit = onExit
            )

            else -> Unit
        }
    }
}
