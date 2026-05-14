package com.cybermaze.features.level_06

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.cybermaze.core.ui.renderer.LevelBackgroundLayer
import com.cybermaze.core.ui.renderer.LevelBackgroundStyle
import com.cybermaze.core.ui.renderer.TeleportPulseLayer

/**
 * Screen for Level 06 "Warp Zone".
 */
@Composable
fun Level06Screen(
    viewModel: Level06ViewModel = hiltViewModel(),
    onExit: () -> Unit = {},
    onNextLevel: () -> Unit = {}
) {
    val state = viewModel.gameState.collectAsState().value

    if (state == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LEVEL_06_PALETTE.background),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "LOADING WARP ZONE…", color = LEVEL_06_PALETTE.hudTitle)
        }
        return
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

        TeleportPulseLayer(gameState = state, modifier = Modifier.fillMaxSize())

        GameHUD(
            levelTitle = state.config.title,
            levelNumber = state.config.levelNumber,
            lives = state.player.lives,
            score = state.player.score,
            collectedPoints = state.collectedPoints,
            totalPoints = state.config.totalPoints,
            timeRemaining = state.remainingTime(),
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
                LevelCompleteScreen(
                    levelNumber = state.config.levelNumber,
                    levelTitle = state.config.title,
                    score = state.player.score,
                    timeSeconds = state.elapsedTime,
                    stars = viewModel.calculateStars(state),
                    hasNextLevel = true,
                    onNextLevel = onNextLevel,
                    onRestart = viewModel::onRestart,
                    onExit = onExit,
                    timeLimit = state.config.timeLimit,
                    totalEnergy = viewModel.totalEnergy(),
                    collectedEnergy = state.collectedPoints,
                    livesLost = state.livesLost
                )
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
