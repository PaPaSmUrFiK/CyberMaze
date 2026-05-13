package com.cybermaze.features.level_05

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
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
import com.cybermaze.core.ui.renderer.MovingTrapsLayer
import kotlin.math.ceil
import androidx.compose.foundation.clickable
/**
 * Screen for Level 05 — "Trap Matrix".
 *
 * Features moving traps rendered as an overlay layer.
 * Disarm button appears in center after collecting 10 energy points.
 */
@Composable
fun Level05Screen(
    viewModel: Level05ViewModel = hiltViewModel(),
    onExit: () -> Unit = {},
    onNextLevel: () -> Unit = {}
) {
    val state = viewModel.gameState.collectAsState().value
    val areTrapsDisabled by viewModel.areTrapsDisabled.collectAsState()
    val isDisarmButtonAvailable by viewModel.isDisarmButtonAvailable.collectAsState()

    // Проверяем доступность кнопки при каждом обновлении collectedPoints
    LaunchedEffect(state?.collectedPoints) {
        viewModel.checkDisarmButtonAvailability()
    }

    if (state == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LEVEL_05_PALETTE.background),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "INITIALIZING TRAP MATRIX…", color = LEVEL_05_PALETTE.hudTitle)
        }
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(state.palette.background)
            .swipeDirections(onDirection = viewModel::onDirectionInput)
    ) {
        // 1. Game canvas
        GameCanvas(
            gameState = state,
            modifier = Modifier.fillMaxSize()
        )

        // 2. Moving traps overlay (pulsing red diamonds) — only if not disabled
        if (!areTrapsDisabled) {
            MovingTrapsLayer(
                gameState = state,
                modifier = Modifier.fillMaxSize()
            )
        }

        // 3. HUD
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

        // 4. Кнопка обезвреживания ловушек (по центру, появляется после сбора 10 точек)
        if (state.phase == GamePhase.PLAYING && !areTrapsDisabled && isDisarmButtonAvailable) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(100.dp, 80.dp)
                    .background(
                        Color.Black.copy(alpha = 0.85f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .border(
                        2.dp,
                        Color.Red.copy(alpha = 0.8f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "⚠️",
                        fontSize = 28.sp
                    )
                    Text(
                        text = "DISARM TRAPS",
                        color = Color.Red,
                        fontSize = 10.sp
                    )
                    Text(
                        text = "CLICK HERE",
                        color = Color.Red.copy(alpha = 0.7f),
                        fontSize = 8.sp
                    )
                }
            }

            // Прозрачная кликабельная область поверх бокса
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(100.dp, 80.dp)
                    .background(Color.Transparent)
                    .clickable { viewModel.disableTrapsTemporarily() }
            )
        }

        // 5. Индикатор "ловушки отключены"
        if (state.phase == GamePhase.PLAYING && areTrapsDisabled) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(100.dp, 60.dp)
                    .background(
                        Color.Black.copy(alpha = 0.85f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .border(
                        2.dp,
                        Color.Green.copy(alpha = 0.8f),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "✓ TRAPS DISABLED",
                    color = Color.Green,
                    fontSize = 12.sp
                )
            }
        }

        // 6. Подсказка о сборе точек для активации кнопки
        if (state.phase == GamePhase.PLAYING && !areTrapsDisabled && !isDisarmButtonAvailable && state.collectedPoints < 10) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(120.dp, 50.dp)
                    .background(
                        Color.Black.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Collect ${10 - state.collectedPoints} more to\ndisable traps!",
                    color = Color.Yellow,
                    fontSize = 10.sp
                )
            }
        }

        // 7. D-Pad
        if (state.phase == GamePhase.PLAYING) {
            DPadControl(
                onDirection = viewModel::onDirectionInput,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 16.dp)
                    .alpha(0.7f)
            )
        }

        // 8. Phase overlays
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