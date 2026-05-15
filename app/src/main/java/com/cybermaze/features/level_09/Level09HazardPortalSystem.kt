package com.cybermaze.features.level_09

import androidx.compose.ui.graphics.Color
import com.cybermaze.core.game.engine.GameEventEmitter
import com.cybermaze.core.game.engine.LevelSystem
import com.cybermaze.core.game.model.Enemy
import com.cybermaze.core.game.model.EnemyType
import com.cybermaze.core.game.model.GamePhase
import com.cybermaze.core.game.model.GameState
import com.cybermaze.core.game.model.Position

/**
 * 1. Hazard cycle — [SAFE_SECONDS] safe / [HAZARD_SECONDS] red. When **frenzy starts**, hazard is
 *    forced to **red** (timer reset) so rage and red portals coincide from the first frame of rage.
 *    Player on a real teleport pad during red takes damage (decoy pad is never a teleport tile).
 *
 * 2. Portal guardian warp (once per run):
 *    When **frenzy + red hazard** and [PORTAL_CHASER_ID] stands on portal **A**, a **short ring preview**
 *    ([PREVIEW_SECONDS] s) starts, then **[DECOY_HOLD_SECONDS] s** where the decoy stays active while the
 *    guardian remains clamped on A; only after that it becomes CHASER and snaps to the destination.
 *    Destination alternates **bottom / top** between level restarts (first run random).
 */
class Level09HazardPortalSystem(private val frenzySystem: Level09FrenzySystem) : LevelSystem {

    /** Tracks frenzy edge for syncing hazard red with rage start. */
    private var wasFrenzy: Boolean = false

    private var hazardTimer: Float = 0f
    private var isHazard: Boolean = false

    private var warpState: WarpState = WarpState.Idle
    private var previewTimer: Float = 0f
    private var chosenDest: Position? = null

    /** Alternates bottom/top between level runs (not reset in [reset]). */
    private var nextWarpIsBottom: Boolean = kotlin.random.Random.nextBoolean()

    fun isHazardActive(): Boolean = isHazard

    /** Decoy portal world position — only while previewing (for UI overlay). */
    fun decoyWarpPosition(): Position? = if (warpState == WarpState.Previewing) chosenDest else null

    fun isWarpPreviewActive(): Boolean = warpState == WarpState.Previewing

    fun reset() {
        hazardTimer = 0f
        isHazard = false
        warpState = WarpState.Idle
        previewTimer = 0f
        chosenDest = null
        wasFrenzy = false
        // Deliberately do NOT reset [nextWarpIsBottom] — alternates across restarts.
    }

    override fun update(state: GameState, deltaTime: Float, emit: GameEventEmitter): GameState {
        if (state.phase != GamePhase.PLAYING) return tintPortals(state)

        // Align red hazard with frenzy start: the moment rage begins, portals go red immediately.
        val frenzyNow = frenzySystem.isFrenzyActive()
        if (frenzyNow && !wasFrenzy) {
            isHazard = true
            hazardTimer = 0f
        }
        wasFrenzy = frenzyNow

        hazardTimer += deltaTime
        val cycleDuration = if (isHazard) HAZARD_SECONDS else SAFE_SECONDS
        if (hazardTimer >= cycleDuration) {
            hazardTimer -= cycleDuration
            isHazard = !isHazard
        }

        var next = when (warpState) {
            WarpState.Idle -> maybeStartPreview(state)
            WarpState.Previewing -> advancePreview(state, deltaTime)
            WarpState.Done -> state
        }

        return tintPortals(next)
    }

    override fun onPlayerLanded(state: GameState, emit: GameEventEmitter): GameState {
        if (!isHazard) return state
        val player = state.player
        val onPortal = state.teleports.any {
            it.posA == player.position || it.posB == player.position
        }
        if (!onPortal || player.isInvincible) return state

        val damagedPlayer = if (player.hasShield) {
            player.removeShield().grantInvincibility()
        } else {
            player.loseLife().grantInvincibility()
        }
        return state.updatePlayer(damagedPlayer)
    }

    // -------------------------------------------------------------------------
    // Warp FSM
    // -------------------------------------------------------------------------

    private fun maybeStartPreview(state: GameState): GameState {
        if (!frenzySystem.isFrenzyActive() || !isHazard) return state

        val portalAPos = state.teleports.firstOrNull()?.posA ?: FALLBACK_PORTAL_A
        val portalEnemy = state.enemies.find { it.id == PORTAL_CHASER_ID && it.isActive } ?: return state
        if (portalEnemy.position != portalAPos) return state

        warpState = WarpState.Previewing
        previewTimer = 0f
        chosenDest = pickWarpDestinationAvoidingPlayer(state)

        // Clamp guardian onto A for ring preview + decoy hold ([PREVIEW_SECONDS] + [DECOY_HOLD_SECONDS]).
        return state.updateEnemies(snapPortalEnemy(state.enemies, portalAPos))
    }

    private fun advancePreview(state: GameState, deltaTime: Float): GameState {
        val portalAPos = state.teleports.firstOrNull()?.posA ?: FALLBACK_PORTAL_A
        previewTimer += deltaTime

        // Keep clamped on A while decoy is active (ring + hold).
        var next = state.updateEnemies(snapPortalEnemy(state.enemies, portalAPos))

        val previewEnd = PREVIEW_SECONDS + DECOY_HOLD_SECONDS
        if (previewTimer >= previewEnd) {
            val dest = chosenDest ?: PORTAL_SPAWN_BOTTOM
            warpState = WarpState.Done
            chosenDest = null
            previewTimer = 0f
            next = teleportAndPromote(next, portalAPos, dest)
        }
        return next
    }

    private fun snapPortalEnemy(enemies: List<Enemy>, at: Position) =
        enemies.map { e ->
            if (e.id == PORTAL_CHASER_ID && e.isActive) {
                e.copy(previousPosition = at, position = at)
            } else e
        }

    private fun teleportAndPromote(state: GameState, fromPos: Position, dest: Position): GameState {
        val safeDest = resolveDestAvoidingPlayer(state, dest)
        val updated = state.enemies.map { e ->
            if (e.id == PORTAL_CHASER_ID && e.isActive) {
                e.copy(
                    type = EnemyType.CHASER,
                    patrolPath = emptyList(),
                    patrolIndex = 0,
                    previousPosition = fromPos,
                    position = safeDest
                )
            } else e
        }
        return state.updateEnemies(updated)
    }

    private fun resolveDestAvoidingPlayer(state: GameState, primary: Position): Position {
        if (state.player.position != primary) return primary
        val fallbacks = listOf(
            primary.copy(x = primary.x + 1),
            primary.copy(x = primary.x - 1),
            primary.copy(y = primary.y + 1),
            primary.copy(y = primary.y - 1)
        )
        return fallbacks.firstOrNull { it != state.player.position } ?: primary
    }

    private fun pickWarpDestinationAvoidingPlayer(state: GameState): Position {
        val wantBottom = nextWarpIsBottom
        nextWarpIsBottom = !nextWarpIsBottom
        val base = if (wantBottom) PORTAL_SPAWN_BOTTOM else PORTAL_SPAWN_TOP
        return resolveDestAvoidingPlayer(state, base)
    }

    private fun tintPortals(state: GameState): GameState {
        if (!isHazard) return state
        val red = Color(0xFFFF1744)
        return state.copy(palette = state.palette.copy(teleportA = red, teleportB = red))
    }

    private enum class WarpState { Idle, Previewing, Done }

    companion object {
        const val SAFE_SECONDS: Float = 15f
        const val HAZARD_SECONDS: Float = 5f
        /** Ring / first beat of the decoy VFX. */
        const val PREVIEW_SECONDS: Float = 0.5f
        /** Decoy stays “active” on the map; guardian still pinned on A until this elapses. */
        const val DECOY_HOLD_SECONDS: Float = 0.5f
        const val PORTAL_CHASER_ID: String = "chaser_portal"

        val PORTAL_SPAWN_BOTTOM: Position = Position(7, 11)
        /** Upper warp anchor — highest open row on the left (straight corridor under top wall). */
        val PORTAL_SPAWN_TOP: Position = Position(10, 1)
        val FALLBACK_PORTAL_A: Position = Position(5, 5)
    }
}
