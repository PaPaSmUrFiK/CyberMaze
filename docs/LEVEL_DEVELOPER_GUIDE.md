# Level developer guide

This document explains how to add or modify levels **2–10** using the reusable engine introduced in `core/game/engine` and `core/game/system`. The authoritative game design reference remains **[CyberMaze_TZ.md](../CyberMaze_TZ.md)**.

**Level 7:** In the TZ, “Blackout” centers on fog-of-war; the shipped **Level 7** (“Unicorn Meadow”, `features/level_07/`) keeps the **same ASCII maze footprint** but **does not** use `FogOfWarSystem` / `FogOfWarLayer` or `baseFogRadiusTiles`. Difficulty is from **guard vision cones** and **moving traps** on the full visible map instead.

## 1. Quick workflow

1. **Copy** the donor package `app/src/main/java/com/cybermaze/features/level_template/` to e.g. `features/level_05/`.
2. **Rename** types and files: `LevelTemplate*` → `Level05*`, package `com.cybermaze.features.level_05`.
3. **Edit the map** (`Level05Map.kt`): every row same width; include `X` (spawn) and `E` (exit). Legend → [LevelLoader.kt](../app/src/main/java/com/cybermaze/core/game/level/LevelLoader.kt).
4. **Tune** `LevelConfig` (points, time limit, flags) and **enemy / trap / teleport** data files.
5. **Pick systems**: build `LevelDefinition.extraSystems` with only the mechanics you need (see table below).
6. **Pick UI layers** in your `LevelXXScreen`: stack optional composables above `GameCanvas` (same `Box` as Level 01 / template).
7. **Register navigation**: replace the placeholder route in [NavGraph.kt](../app/src/main/java/com/cybermaze/core/navigation/NavGraph.kt) with your screen.
8. **Level select title**: add the display name in [LevelSelectViewModel.kt](../app/src/main/java/com/cybermaze/features/levelselect/LevelSelectViewModel.kt) if needed.

## 2. Mechanic → `LevelSystem` → overlay layer

| Mechanic (TZ) | `LevelSystem` | Optional render layer |
|---------------|---------------|------------------------|
| Slow enemies after `P` pickup | `PowerUpSystem` | — (HUD shows countdown via `GameHUD.powerUpSecondsLeft`) |
| Player speed `S` | `SpeedBoostSystem` (hook placeholder; timing in `Player`) | — (`GameHUD.speedBoostSecondsLeft`) |
| Teleport pads `A`/`B` | `TeleportSystem` | `TeleportPulseLayer` |
| Moving hazard along a path | `MovingTrapSystem(collisionSystem)` | `MovingTrapsLayer` |
| Guard cone vision | `GuardSystem(collisionSystem)` | `GuardVisionLayer` |
| Fog of war + vision boost on `P` | `FogOfWarSystem` + `LevelDefinition.baseFogRadiusTiles` | `FogOfWarLayer` |

**Construction note:** `MovingTrapSystem` and `GuardSystem` need a `CollisionSystem` instance (inject in `@HiltViewModel` like the template).

## 3. `LevelDefinition` checklist

- `config` / `map` / `enemies` — required.
- `traps` / `teleports` — default empty.
- `baseFogRadiusTiles` — `null` = no fog; integer = fog enabled for `FogOfWarSystem` + `FogOfWarLayer`.
- `extraSystems` — ordered list; if two systems react to the same collectible, **order matters** (template puts `PowerUpSystem` before `FogOfWarSystem` for `POWER_UP`).
- `palette` — optional [LevelPalette](../app/src/main/java/com/cybermaze/core/ui/theme/LevelPalette.kt) (see section 7).

## 4. Per-level theming (reskin)

Every renderer reads its colors from [LevelPalette](../app/src/main/java/com/cybermaze/core/ui/theme/LevelPalette.kt). To re-skin your level:

1. Create `LevelXXPalette.kt` in your level package. Override only the fields you want to change — every field defaults to the original Cyber Maze palette:

   ```kotlin
   val LEVEL_05_PALETTE = LevelPalette(
       background = Color(0xFF0F0028),
       wallFill = Color(0xFF2D004B),
       player = Color(0xFFE5FF00),
       enemyChaser = Color(0xFFFF0066)
   )
   ```

2. Attach it to your `LevelDefinition`:

   ```kotlin
   setupLevel(
       LevelDefinition(
           config = cfg,
           map = map,
           enemies = enemies,
           palette = LEVEL_05_PALETTE
       )
   )
   ```

3. Pass `palette = state.palette` into [GameHUD](../app/src/main/java/com/cybermaze/core/ui/components/GameHUD.kt) (other renderers pick it up automatically from `GameState`).

### Custom background

To replace the default grid background:

1. In `GameCanvas(...)` call pass `drawBackground = false`.
2. Add [LevelBackgroundLayer](../app/src/main/java/com/cybermaze/core/ui/renderer/LevelBackgroundLayer.kt) (or any Composable you write) **below** `GameCanvas` in the `Box` stack — its `palette` arg drives its colors:

   ```kotlin
   Box(Modifier.fillMaxSize().background(state.palette.background)) {
       LevelBackgroundLayer(palette = state.palette, style = LevelBackgroundStyle.Radial)
       GameCanvas(gameState = state, drawBackground = false)
       // ... overlays + HUD
   }
   ```

`LevelBackgroundLayer` has three built-in styles (`Grid`, `Radial`, `ScanLines`) and an overload that accepts arbitrary `content: @Composable BoxScope.() -> Unit` when none of them fit.

## 5. Stars and the briefing screen

Star rules live in [LevelResult.kt](../app/src/main/java/com/cybermaze/core/game/level/LevelResult.kt) and are universal:

| Star | Condition |
|------|-----------|
| 1st | Level completed (reach `E` with `collectedPoints >= requiredPoints`) |
| 2nd | `timeSeconds <= timeLimit * 0.75` (requires a non-null `timeLimit`) |
| 3rd | Collect every energy point **and** lose no lives |

Two UI surfaces explain these conditions to the player:

- **Before the level**: [LevelBriefingScreen](../app/src/main/java/com/cybermaze/core/ui/components/LevelBriefingScreen.kt) is shown automatically when `GameState.phase == GamePhase.BRIEFING`. Every level starts in this phase. The player taps `START` (calls `BaseLevelViewModel.onStart()`) to transition to `PLAYING`.
- **After the level**: [LevelCompleteScreen](../app/src/main/java/com/cybermaze/core/ui/components/LevelCompleteScreen.kt) shows each condition with a `✓` / `✗` mark and explains the miss (e.g. "missed 3 energy", "died once"). Pass these from your screen:
  ```kotlin
  LevelCompleteScreen(
      // ...existing args...
      timeLimit = state.config.timeLimit,
      totalEnergy = viewModel.totalEnergy(),
      collectedEnergy = state.collectedPoints,
      livesLost = state.livesLost
  )
  ```

Render `LevelBriefingScreen` in your `when (state.phase)` block:
```kotlin
GamePhase.BRIEFING -> LevelBriefingScreen(
    config = state.config,
    onStart = viewModel::onStart,
    palette = state.palette
)
```

## 6. PR checklist

- [ ] Map validates with `LevelLoader.validateLayout(...).isEmpty()`.
- [ ] `requiredPoints` / `totalPoints` match energy tiles (`LevelLoader.countTiles(map, 'o')`).
- [ ] Restart resets authored entities (enemies/traps/teleports); no stale `powerUpSecondsLeft` / `fogVisionBoostSecondsLeft`.
- [ ] Only needed `LevelSystem`s and layers are included (no unused fog/trap layers).
- [ ] `NavGraph` destination + back stack arguments updated.
- [ ] `./gradlew :app:assembleDebug` passes.

## 7. Minimal example

See **Level 01** — `Level01ViewModel` calls `setupLevel(LevelDefinition(...))` with **no** extras: the base engine (movement, enemies, collectibles, win/lose) is enough for a full level.

## 8. Full “Lego” example

See **`features/level_template/`** — demonstrates **all** systems, overlay layers, a custom palette ([LevelTemplatePalette.kt](../app/src/main/java/com/cybermaze/features/level_template/LevelTemplatePalette.kt)) and a custom animated background. It is **not** registered in `NavGraph` (copy-only donor).
