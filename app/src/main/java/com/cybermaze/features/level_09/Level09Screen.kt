package com.cybermaze.features.level_09

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.cybermaze.core.ui.renderer.TeleportPulseLayer
import com.cybermaze.core.ui.theme.CyberColors
import kotlin.math.ceil

@Composable
fun Level09Screen(
    viewModel: Level09ViewModel = hiltViewModel(),
    onExit: () -> Unit = {},
    onNextLevel: () -> Unit = {}
) {
    val state = viewModel.gameState.collectAsState().value
    val isFrenzy = viewModel.isFrenzyActive()
    val frenzyPulse = viewModel.frenzyPulse01()
    val frenzySwitchSeconds = viewModel.secondsToFrenzyModeSwitch()
    val warpDecoy = viewModel.warpDecoyPosition()
    val isWarpPreview = viewModel.isWarpPreviewActive()
    val isAfkWarning = viewModel.isAfkWarning()

    if (state == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CyberColors.Background),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "INITIALIZING DEEP MAZE…", color = CyberColors.NeonCyan)
        }
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(state.palette.background)
            .swipeDirections(onDirection = viewModel::onDirectionInput)
    ) {
        GameCanvas(
            gameState = state,
            modifier = Modifier.fillMaxSize()
        )

        TeleportPulseLayer(
            gameState = state,
            modifier = Modifier.fillMaxSize()
        )

        Level09WarpVfx(
            gameState = state,
            decoyPosition = warpDecoy,
            isWarpPreview = isWarpPreview,
            modifier = Modifier.fillMaxSize()
        )

        // Frenzy tension overlay: translucent red veil with soft pulsing alpha.
        if (isFrenzy) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFF3047).copy(alpha = 0.15f + 0.20f * frenzyPulse))
            )
        }

        // AFK warning — centered, appears after 3 s of standing still.
        if (isAfkWarning) {
            Text(
                text = "MOVE!",
                color = Color(0xFFFF1744),
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(bottom = 80.dp)
                    .alpha(0.92f)
            )
        }

        // Frenzy countdown — always red, sits left of the main timer in the HUD bar.
        Text(
            text = if (isFrenzy) "RAGE! ${frenzySwitchSeconds}s" else "RAGE IN ${frenzySwitchSeconds}s",
            color = Color(0xFFFF1744),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 8.dp, end = 100.dp)
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
