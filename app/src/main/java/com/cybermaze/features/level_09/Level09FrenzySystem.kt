package com.cybermaze.features.level_09

import com.cybermaze.core.game.engine.GameEventEmitter
import com.cybermaze.core.game.engine.LevelSystem
import com.cybermaze.core.game.model.EnemyType
import com.cybermaze.core.game.model.GameState

/**
 * Cyclic enemy pressure for Level 9:
 *  - 30 s normal mode — enemies at ×1 speed.
 *  - 10 s frenzy mode — red palette, all enemies accelerate:
 *      • CHASER / PATROL / GUARD / FAST: ×[FRENZY_SPEED_MULTIPLIER] (4×)
 *      • RANDOM enemies:  temporarily become CHASER type at ×[FRENZY_RANDOM_MULTIPLIER] (3.5×).
 *        Their type is restored to RANDOM when frenzy ends.
 */
class Level09FrenzySystem : LevelSystem {
    private var isFrenzy: Boolean = false
    private var modeTimer: Float = 0f
    private var pulseTimer: Float = 0f
    private var secondsUntilSwitch: Int = NORMAL_SECONDS.toInt()

    override fun update(state: GameState, deltaTime: Float, emit: GameEventEmitter): GameState {
        if (state.phase.isActivePlay().not()) return applyMode(state)

        modeTimer += deltaTime
        pulseTimer += deltaTime

        val duration = if (isFrenzy) FRENZY_SECONDS else NORMAL_SECONDS
        if (modeTimer >= duration) {
            modeTimer -= duration
            isFrenzy = !isFrenzy
        }

        secondsUntilSwitch = kotlin.math.ceil(
            ((if (isFrenzy) FRENZY_SECONDS else NORMAL_SECONDS) - modeTimer).coerceAtLeast(0f).toDouble()
        ).toInt()

        return applyMode(state)
    }

    fun isFrenzyActive(): Boolean = isFrenzy

    fun frenzyPulse01(): Float {
        val raw = kotlin.math.sin(pulseTimer * 7.5f)
        return (0.5f + 0.5f * raw).coerceIn(0f, 1f)
    }

    fun secondsToNextMode(): Int = secondsUntilSwitch

    fun reset() {
        isFrenzy = false
        modeTimer = 0f
        pulseTimer = 0f
        secondsUntilSwitch = NORMAL_SECONDS.toInt()
    }

    private fun applyMode(state: GameState): GameState {
        val targetPalette = if (isFrenzy) LEVEL_09_PALETTE_FRENZY else LEVEL_09_PALETTE_NORMAL
        val adjustedEnemies = state.enemies.map { enemy ->
            when {
                // Frenzy: random walkers turn into chasers at reduced speed multiplier.
                isFrenzy && enemy.id in RANDOM_ENEMY_IDS ->
                    enemy.copy(type = EnemyType.CHASER, speedMultiplier = FRENZY_RANDOM_MULTIPLIER)
                // Frenzy: all other enemies get the full multiplier.
                isFrenzy ->
                    enemy.withSpeedMultiplier(FRENZY_SPEED_MULTIPLIER)
                // Normal: restore random walkers to their original type.
                enemy.id in RANDOM_ENEMY_IDS ->
                    enemy.copy(type = EnemyType.RANDOM, speedMultiplier = 1f)
                // Normal: reset speed multiplier for everyone else.
                else ->
                    enemy.withSpeedMultiplier(1f)
            }
        }
        return state.copy(enemies = adjustedEnemies, palette = targetPalette)
    }

    private fun com.cybermaze.core.game.model.GamePhase.isActivePlay() =
        this == com.cybermaze.core.game.model.GamePhase.PLAYING

    companion object {
        const val NORMAL_SECONDS: Float = 30f
        const val FRENZY_SECONDS: Float = 10f
        /** Speed multiplier for chasers/patrols/guard during frenzy. */
        const val FRENZY_SPEED_MULTIPLIER: Float = 4f
        /** Speed multiplier for random→chaser enemies during frenzy. */
        const val FRENZY_RANDOM_MULTIPLIER: Float = 3.5f

        /** IDs of enemies that transform during frenzy. */
        val RANDOM_ENEMY_IDS: Set<String> = setOf("random_01", "random_02")
    }
}
