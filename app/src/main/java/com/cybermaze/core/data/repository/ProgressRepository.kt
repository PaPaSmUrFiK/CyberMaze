package com.cybermaze.core.data.repository

import com.cybermaze.core.data.database.dao.LevelRecordDao
import com.cybermaze.core.data.database.dao.PlayerProgressDao
import com.cybermaze.core.data.database.entity.LevelRecordEntity
import com.cybermaze.core.data.database.entity.PlayerProgressEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Persists level progress, leaderboard records and unlock state.
 *
 * `completeLevel` is idempotent — replaying a level only updates the
 * best score / fastest time / max stars, never downgrades them.
 */
@Singleton
class ProgressRepository @Inject constructor(
    private val progressDao: PlayerProgressDao,
    private val recordDao: LevelRecordDao
) {

    fun getAllProgress(): Flow<List<PlayerProgressEntity>> = progressDao.getAllProgress()

    suspend fun getProgressForLevel(levelNumber: Int): PlayerProgressEntity? =
        progressDao.getProgressForLevel(levelNumber)

    /**
     * Record a level completion: keep the best score/time/stars and unlock
     * the next level. Always inserts an entry in the records history.
     */
    suspend fun completeLevel(
        levelNumber: Int,
        score: Int,
        timeSeconds: Float,
        stars: Int
    ) {
        val existing = progressDao.getProgressForLevel(levelNumber)
        val previousBestTime = existing?.bestTimeSeconds ?: 0f
        val newBestTime = when {
            previousBestTime <= 0f -> timeSeconds
            else -> minOf(previousBestTime, timeSeconds)
        }
        val updated = PlayerProgressEntity(
            levelNumber = levelNumber,
            isUnlocked = true,
            isCompleted = true,
            starsEarned = maxOf(existing?.starsEarned ?: 0, stars),
            bestScore = maxOf(existing?.bestScore ?: 0, score),
            bestTimeSeconds = newBestTime,
            totalAttempts = (existing?.totalAttempts ?: 0) + 1,
            lastPlayedAt = System.currentTimeMillis()
        )
        progressDao.upsertProgress(updated)

        recordDao.insertRecord(
            LevelRecordEntity(
                levelNumber = levelNumber,
                score = score,
                timeSeconds = timeSeconds,
                starsEarned = stars
            )
        )

        if (levelNumber < TOTAL_LEVELS) unlockLevel(levelNumber + 1)
    }

    /** Make sure `levelNumber` exists and is unlocked. */
    suspend fun unlockLevel(levelNumber: Int) {
        val existing = progressDao.getProgressForLevel(levelNumber)
        if (existing == null) {
            progressDao.upsertProgress(
                PlayerProgressEntity(levelNumber = levelNumber, isUnlocked = true)
            )
        } else if (!existing.isUnlocked) {
            progressDao.unlockLevel(levelNumber)
        }
    }

    /** Seed all 10 progress rows on the very first launch. Level 1 is unlocked. */
    suspend fun initializeProgress() {
        val level1 = progressDao.getProgressForLevel(1)
        if (level1 == null) {
            for (i in 1..TOTAL_LEVELS) {
                progressDao.upsertProgress(
                    PlayerProgressEntity(
                        levelNumber = i,
                        isUnlocked = i == 1
                    )
                )
            }
        }
    }

    /**
     * Force-unlock every level. Used by the testing toggle in
     * [com.cybermaze.features.levelselect.LevelSelectViewModel] so QA / devs can
     * jump straight into any level without grinding through the unlock chain.
     *
     * Existing progress (stars, best score, best time) is preserved; only the
     * `isUnlocked` flag is set.
     */
    suspend fun unlockAllLevels() {
        for (i in 1..TOTAL_LEVELS) {
            val existing = progressDao.getProgressForLevel(i)
            if (existing == null) {
                progressDao.upsertProgress(
                    PlayerProgressEntity(levelNumber = i, isUnlocked = true)
                )
            } else if (!existing.isUnlocked) {
                progressDao.unlockLevel(i)
            }
        }
    }

    fun getTotalScore(): Flow<Int?> = progressDao.getTotalScore()
    fun getTotalStars(): Flow<Int?> = progressDao.getTotalStars()
    fun getCompletedLevelsCount(): Flow<Int> = progressDao.getCompletedLevelsCount()
    fun getTopRecordsForLevel(levelNumber: Int): Flow<List<LevelRecordEntity>> =
        recordDao.getTopRecordsForLevel(levelNumber)

    companion object {
        const val TOTAL_LEVELS: Int = 10
    }
}
