package com.cybermaze.core.game.level

/**
 * Represents the result of completing a level.
 * Used for calculating stars and saving progress.
 */
data class LevelResult(
    val levelNumber: Int,
    val completed: Boolean,
    val score: Int,
    val timeSeconds: Float,
    val starsEarned: Int,
    val allPointsCollected: Boolean,
    val noLivesLost: Boolean
) {
    companion object {
        /**
         * Calculates stars earned based on performance.
         * 
         * ⭐ = Level completed
         * ⭐⭐ = Completed quickly (within 75% of time limit)
         * ⭐⭐⭐ = Perfect (all points collected, no lives lost)
         */
        fun calculateStars(
            completed: Boolean,
            timeSeconds: Float,
            timeLimit: Int?,
            allPointsCollected: Boolean,
            noLivesLost: Boolean
        ): Int {
            if (!completed) return 0
            
            // 1 star for completion
            var stars = 1
            
            // 2nd star for fast completion
            if (timeLimit != null && timeSeconds <= timeLimit * 0.75f) {
                stars = 2
            }
            
            // 3rd star for perfect run
            if (allPointsCollected && noLivesLost) {
                stars = 3
            }
            
            return stars
        }
    }
}
