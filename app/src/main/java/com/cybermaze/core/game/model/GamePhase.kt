package com.cybermaze.core.game.model

/**
 * Represents the current phase of the game.
 */
enum class GamePhase {
    LOADING,    // Level is loading
    BRIEFING,   // Pre-game overlay shown before the player taps START
    PLAYING,    // Game is actively being played
    PAUSED,     // Game is paused
    WIN,        // Player won the level
    LOSE        // Player lost the level
}
