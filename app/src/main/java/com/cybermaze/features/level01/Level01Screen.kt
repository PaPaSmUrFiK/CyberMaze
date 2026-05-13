package com.cybermaze.features.level01

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.cybermaze.core.ui.renderer.GameCanvas
import com.cybermaze.core.ui.theme.CyberColors
import kotlin.math.ceil

/**
 * Screen for Level 01 — "Boot Sector".
 *
 * Layered Box (back → front):
 *   1. GameCanvas (also captures swipe input)
 *   2. HUD (top)
 *   3. D-Pad overlay (bottom-left, translucent)
 *   4. State overlays: pause / win / lose
 *
 * Other levels are encouraged to follow this exact pattern.
 */
@Composable
fun Level01Screen(
    viewModel: Level01ViewModel = hiltViewModel(),
    onExit: () -> Unit = {},
    onNextLevel: () -> Unit = {}
) {
    val state = viewModel.gameState.collectAsState().value

    if (state == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CyberColors.Background),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "INITIALIZING...", color = CyberColors.NeonCyan)
        }
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberColors.Background)
            .swipeDirections(onDirection = viewModel::onDirectionInput)
    ) {
        // 1. Game canvas (full screen)
        GameCanvas(
            gameState = state,
            modifier = Modifier.fillMaxSize()
        )

        // 2. HUD
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

        // 3. D-Pad
        if (state.phase == GamePhase.PLAYING) {
            DPadControl(
                onDirection = viewModel::onDirectionInput,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 16.dp)
                    .alpha(0.7f)
            )
        }

        // 4. Overlays
        when (state.phase) {
            GamePhase.PAUSED -> PauseMenu(
                onResume = viewModel::onResume,
                onRestart = viewModel::onRestart,
                onExit = onExit
            )
            GamePhase.WIN -> LevelCompleteScreen(
                levelNumber = state.config.levelNumber,
                levelTitle = state.config.title,
                score = state.player.score,
                timeSeconds = state.elapsedTime,
                stars = viewModel.calculateStars(state),
                hasNextLevel = state.config.levelNumber < 10,
                onNextLevel = onNextLevel,
                onRestart = viewModel::onRestart,
                onExit = onExit,
                timeLimit = state.config.timeLimit,
                totalEnergy = viewModel.totalEnergy(),
                collectedEnergy = state.collectedPoints,
                livesLost = state.livesLost
            )
            GamePhase.BRIEFING -> LevelBriefingScreen(
                config = state.config,
                onStart = viewModel::onStart,
                palette = state.palette
            )
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
