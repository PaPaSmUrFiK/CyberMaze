package com.cybermaze.features.levelselect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cybermaze.core.data.repository.ProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Exposes level cards for [LevelSelectScreen].
 *
 * The first launch may have no DB rows yet — the screen still needs to show
 * 10 cards, so we synthesize defaults (only level 1 unlocked) when nothing
 * is persisted. We also fire [ProgressRepository.initializeProgress] so the
 * underlying rows exist on subsequent launches.
 */
@HiltViewModel
class LevelSelectViewModel @Inject constructor(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _levels = MutableStateFlow(defaultLevels())
    val levels: StateFlow<List<LevelInfo>> = _levels.asStateFlow()

    init {
        viewModelScope.launch {
            progressRepository.initializeProgress()
            if (UNLOCK_ALL_FOR_TESTING) {
                progressRepository.unlockAllLevels()
            }
        }
        viewModelScope.launch {
            progressRepository.getAllProgress().collect { progressList ->
                _levels.value = (1..TOTAL_LEVELS).map { num ->
                    val progress = progressList.find { it.levelNumber == num }
                    val unlocked = when {
                        UNLOCK_ALL_FOR_TESTING -> true
                        else -> progress?.isUnlocked ?: (num == 1)
                    }
                    LevelInfo(
                        levelNumber = num,
                        title = getLevelTitle(num),
                        isUnlocked = unlocked,
                        isCompleted = progress?.isCompleted ?: false,
                        starsEarned = progress?.starsEarned ?: 0,
                        bestScore = progress?.bestScore ?: 0
                    )
                }
            }
        }
    }

    private fun defaultLevels(): List<LevelInfo> = (1..TOTAL_LEVELS).map { num ->
        LevelInfo(
            levelNumber = num,
            title = getLevelTitle(num),
            isUnlocked = UNLOCK_ALL_FOR_TESTING || num == 1
        )
    }

    private fun getLevelTitle(levelNumber: Int): String = when (levelNumber) {
        1 -> "Boot Sector"
        2 -> "Data Harvest"
        3 -> "Speed Protocol"
        4 -> "Locked Grid"
        5 -> "Trap Matrix"
        6 -> "Warp Zone"
        7 -> "Unicorn Meadow"
        8 -> "Hunter Pack"
        9 -> "Deep Maze"
        10 -> "Final Core"
        else -> "Level $levelNumber"
    }

    companion object {
        const val TOTAL_LEVELS: Int = 10

        /**
         * Dev / QA toggle: when `true` every level is treated as unlocked regardless
         * of saved progress and [ProgressRepository.unlockAllLevels] is invoked at
         * startup so the DB stays consistent.
         *
         * Flip to `false` before shipping a public build to restore the linear
         * "complete N to unlock N+1" flow handled by
         * [ProgressRepository.completeLevel].
         */
        const val UNLOCK_ALL_FOR_TESTING: Boolean = true
    }
}
