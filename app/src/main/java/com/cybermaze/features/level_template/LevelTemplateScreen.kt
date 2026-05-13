package com.cybermaze.features.level_template

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
import com.cybermaze.core.ui.renderer.FogOfWarLayer
import com.cybermaze.core.ui.renderer.GameCanvas
import com.cybermaze.core.ui.renderer.GuardVisionLayer
import com.cybermaze.core.ui.renderer.LevelBackgroundLayer
import com.cybermaze.core.ui.renderer.LevelBackgroundStyle
import com.cybermaze.core.ui.renderer.MovingTrapsLayer
import com.cybermaze.core.ui.renderer.TeleportPulseLayer
import kotlin.math.ceil

/**
 * Example level screen with **all** optional render layers stacked above [GameCanvas].
 *
 * - No fog on your level? Remove [FogOfWarLayer] and drop [FogOfWarSystem] + `baseFogRadiusTiles`.
 * - No teleports? Remove [TeleportPulseLayer] and [TeleportSystem].
 * - No moving traps? Remove [MovingTrapsLayer] and [MovingTrapSystem].
 * - No guards? Remove [GuardVisionLayer] and [GuardSystem].
 *
 * This composable is **not** wired into navigation — it exists only as a copy-paste donor.
 */
@Composable
fun LevelTemplateScreen(
    viewModel: LevelTemplateViewModel = hiltViewModel(),
    onExit: () -> Unit = {},
    onNextLevel: () -> Unit = {}
) {
    val state = viewModel.gameState.collectAsState().value

    if (state == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LEVEL_TEMPLATE_PALETTE.background),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "TEMPLATE…", color = LEVEL_TEMPLATE_PALETTE.player)
        }
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(state.palette.background)
            .swipeDirections(onDirection = viewModel::onDirectionInput)
    ) {
        // 0. Custom background layer — drawn BELOW GameCanvas.
        //    Delete this and remove `drawBackground = false` if you want the default look.
        LevelBackgroundLayer(
            palette = state.palette,
            style = LevelBackgroundStyle.Grid,
            modifier = Modifier.fillMaxSize()
        )

        GameCanvas(
            gameState = state,
            modifier = Modifier.fillMaxSize(),
            drawBackground = false
        )

        // Optional overlays (same coordinate space as GameCanvas — full-screen Box).
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
                hasNextLevel = false,
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
