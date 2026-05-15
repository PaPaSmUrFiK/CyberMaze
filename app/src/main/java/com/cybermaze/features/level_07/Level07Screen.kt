package com.cybermaze.features.level_07

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
import androidx.compose.ui.unit.sp
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
import com.cybermaze.core.ui.renderer.GuardVisionLayer
import com.cybermaze.core.ui.renderer.LevelBackgroundLayer
import com.cybermaze.core.ui.renderer.LevelBackgroundStyle
import com.cybermaze.core.ui.renderer.MovingTrapsLayer
import com.cybermaze.core.ui.renderer.TeleportPulseLayer
import kotlin.math.ceil

/** Reserve space under the top [GameHUD] so the map is not drawn underneath it. */
private val Level07GameplayTopInset = 52.dp

/**
 * Level 7 — pastel pink theme, **no** [com.cybermaze.core.ui.renderer.FogOfWarLayer].
 * Tension comes from [GuardVisionLayer] + [MovingTrapsLayer] on the TZ maze layout.
 */
@Composable
fun Level07Screen(
    viewModel: Level07ViewModel = hiltViewModel(),
    onExit: () -> Unit = {},
    onNextLevel: () -> Unit = {}
) {
    val state = viewModel.gameState.collectAsState().value

    if (state == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LEVEL_07_PALETTE.background),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "LOADING UNICORN MEADOW…", color = LEVEL_07_PALETTE.hudTitle)
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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = Level07GameplayTopInset)
        ) {
            GameCanvas(
                gameState = state,
                modifier = Modifier.fillMaxSize(),
                drawBackground = false
            )

            MovingTrapsLayer(gameState = state, modifier = Modifier.fillMaxSize())
            TeleportPulseLayer(gameState = state, modifier = Modifier.fillMaxSize())
            GuardVisionLayer(gameState = state, modifier = Modifier.fillMaxSize())
        }

        Text(
            text = "🦄",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = Level07GameplayTopInset + 4.dp, end = 12.dp)
                .alpha(0.14f),
            fontSize = 28.sp
        )

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
