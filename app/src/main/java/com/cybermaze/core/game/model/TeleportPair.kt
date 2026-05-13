package com.cybermaze.core.game.model

/**
 * Linked teleport pads. Stepping on [posA] warps to [posB] and vice versa.
 * [cooldownA]/[cooldownB] prevent ping-pong until [cooldownDuration] elapses.
 */
data class TeleportPair(
    val id: String,
    val posA: Position,
    val posB: Position,
    val cooldownA: Float = 0f,
    val cooldownB: Float = 0f,
    val cooldownDuration: Float = 1.5f
) {
    fun tickCooldowns(delta: Float): TeleportPair = copy(
        cooldownA = (cooldownA - delta).coerceAtLeast(0f),
        cooldownB = (cooldownB - delta).coerceAtLeast(0f)
    )

    fun triggerFromA(): TeleportPair = copy(cooldownB = cooldownDuration)
    fun triggerFromB(): TeleportPair = copy(cooldownA = cooldownDuration)
}
