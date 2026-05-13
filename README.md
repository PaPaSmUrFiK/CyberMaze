# Cyber Maze

2D grid-based arcade-лабиринт (Pac-Man-like) в киберпанк-стиле. Десять уровней с разной механикой: погоня, патрули, ловушки, телепорты, охранники с зоной видимости, туман войны. Android, Kotlin + Jetpack Compose, Hilt, Room.

Этот README — точка входа в проект. В нём:

1. Где какая документация лежит.
2. Как запустить и собрать.
3. Карта кода: какой файл за что отвечает.
4. Архитектура движка и как переиспользовать / менять компоненты.
5. FAQ по самым частым задачам.

---

## 1. Документация

| Файл | О чём |
|------|--------|
| **`README.md`** *(этот файл)* | Обзор проекта, карта кода, рецепты для типовых задач. |
| [`CyberMaze_TZ.md`](./CyberMaze_TZ.md) | Полное **техническое задание** игры: концепция, баланс, описание всех 10 уровней, прогрессия сложности, UI/UX-гайд, roadmap. Основной дизайн-документ. |
| [`docs/LEVEL_DEVELOPER_GUIDE.md`](./docs/LEVEL_DEVELOPER_GUIDE.md) | Пошаговый **гайд для разработчика уровня 2-10**: workflow, выбор `LevelSystem`, выбор overlay-слоёв, кастомная палитра/фон, условия звёзд, чек-лист PR. |

Если ты пришёл «потрогать код» — сначала этот README. Если «разработать свой уровень» — после README иди в `LEVEL_DEVELOPER_GUIDE`. Если «понять баланс / геймдизайн» — `CyberMaze_TZ.md`.

---

## 2. Запуск

Требования: Android Studio Iguana+, JDK 17, Android SDK 34, эмулятор / устройство с API ≥ 26.

```powershell
# Из корня репозитория, Windows / PowerShell:
.\gradlew.bat :app:assembleDebug          # сборка
.\gradlew.bat :app:installDebug           # установка на подключённое устройство
```

Открыть в Android Studio: `File → Open → выбрать корень проекта → Sync`.

Точка входа Compose-приложения: [`MainActivity.kt`](./app/src/main/java/com/cybermaze/MainActivity.kt) → граф навигации [`core/navigation/NavGraph.kt`](./app/src/main/java/com/cybermaze/core/navigation/NavGraph.kt).

---

## 3. Карта кода

Все Kotlin-исходники живут в `app/src/main/java/com/cybermaze/`. Структура:

```
com/cybermaze/
├── MainActivity.kt                 ─ host Activity, ставит ComposeView + Hilt root
│
├── core/                           ─ переиспользуемые модули, ничего не знают про конкретные уровни
│   ├── data/repository/            ─ Room-репозитории прогресса и настроек
│   ├── game/
│   │   ├── engine/                 ─ ядро движка (см. §4)
│   │   ├── level/                  ─ LevelLoader, парсинг ASCII-карт, LevelResult
│   │   ├── model/                  ─ ИММУТАБЕЛЬНЫЕ data class: Player, Enemy, GameState, Position, …
│   │   └── system/                 ─ LevelSystem-плагины: PowerUp, Teleport, Trap, Guard, Fog…
│   ├── navigation/                 ─ Compose Navigation, маршруты экранов
│   └── ui/
│       ├── components/             ─ переиспользуемые Composable: HUD, кнопки, оверлеи, D-Pad
│       ├── renderer/               ─ всё, что рисует Canvas: GameCanvas + overlay-слои
│       └── theme/                  ─ CyberColors + LevelPalette (палитра под уровень)
│
└── features/                       ─ КАЖДЫЙ УРОВЕНЬ — отдельный пакет
    ├── menu/                       ─ главное меню
    ├── levelselect/                ─ выбор уровня
    ├── settings/                   ─ настройки
    ├── game/                       ─ BaseLevelViewModel — общий контроллер для всех уровней
    ├── level01/                    ─ уровень 1 (готов)
    └── level_template/             ─ ДОНОР: пример уровня со ВСЕМИ системами и слоями (не подключён в навигацию)
```

### 3.1 Файлы, в которые ты будешь заглядывать чаще всего

| Файл | Назначение |
|------|------------|
| [`core/game/engine/LevelDefinition.kt`](./app/src/main/java/com/cybermaze/core/game/engine/LevelDefinition.kt) | «Конструктор уровня»: карта + конфиг + враги + ловушки + телепорты + туман + палитра + список `LevelSystem`. Всё, что отличает уровень N от уровня M, описывается одним `LevelDefinition`. |
| [`core/game/engine/LevelSystem.kt`](./app/src/main/java/com/cybermaze/core/game/engine/LevelSystem.kt) | Интерфейс плагина к движку. 4 хука: `update`, `onPlayerLanded`, `onCollectibleCollected`, `onPlayerHit`. |
| [`features/game/BaseLevelViewModel.kt`](./app/src/main/java/com/cybermaze/features/game/BaseLevelViewModel.kt) | Общий `ViewModel`, на котором сидят все уровни. Управляет фазами, игровым циклом, столкновениями, врагами, звёздами, сохранением прогресса. |
| [`core/game/model/GameState.kt`](./app/src/main/java/com/cybermaze/core/game/model/GameState.kt) | Иммутабельный снапшот текущей сцены. Всё, что рисует UI, читается отсюда. |
| [`core/ui/renderer/GameCanvas.kt`](./app/src/main/java/com/cybermaze/core/ui/renderer/GameCanvas.kt) | Базовый рендер: тайлы, игрок, враги, коллектиблы, выход. Цвета берёт из `gameState.palette`. Поддерживает интерполяцию между тайлами. |
| [`core/ui/components/GameHUD.kt`](./app/src/main/java/com/cybermaze/core/ui/components/GameHUD.kt) | HUD: жизни / счёт / время / счётчики бустов. Параметр `palette` позволяет переокрасить. |
| [`core/ui/theme/LevelPalette.kt`](./app/src/main/java/com/cybermaze/core/ui/theme/LevelPalette.kt) | Палитра уровня — единое место со всеми цветами рендеринга. |
| [`features/level01/Level01ViewModel.kt`](./app/src/main/java/com/cybermaze/features/level01/Level01ViewModel.kt) | Минимальный пример уровня без дополнительных систем. |
| [`features/level_template/`](./app/src/main/java/com/cybermaze/features/level_template/) | Полный пример со ВСЕМИ механиками — донор для копипасты. |

---

## 4. Архитектура движка

### 4.1 Поток данных

```
Игрок ─▶ onDirectionInput / D-Pad / Swipe
            │
            ▼
   BaseLevelViewModel.onTick(delta)          ─── вызывается GameLoop ~60 FPS
            │
            ├─ MovementSystem   (один шаг игрока, если canStep)
            ├─ EnemySystem      (AI и шаги врагов)
            ├─ CollisionSystem  (проверка контактов игрок ↔ враг)
            ├─ HazardDamage     (общая логика урона: щит / неуязвимость / respawn)
            ├─ для каждого LevelSystem из definition.extraSystems:
            │     ├─ sys.update           (тик системы: таймеры, кулдауны)
            │     ├─ sys.onPlayerLanded   (если игрок вошёл в новый тайл)
            │     └─ sys.onPlayerHit      (если CollisionSystem зафиксировал хит)
            └─ checkEndConditions (WIN / LOSE / TIME_UP)
            │
            ▼
   _gameState.value = next     ─── StateFlow перерисовывает Compose
            │
            ▼
   LevelXXScreen рендерит:
            ├─ LevelBackgroundLayer (опц.)
            ├─ GameCanvas
            ├─ overlay-слои: MovingTraps, TeleportPulse, Fog, GuardVision (опц.)
            ├─ GameHUD
            ├─ DPad
            └─ LevelBriefingScreen / PauseMenu / LevelCompleteScreen / GameOverScreen
```

### 4.2 Жизненный цикл `GamePhase`

```
BRIEFING ──tap START──▶ PLAYING ──┐
                            │      ├─ WIN  → LevelCompleteScreen → onRestart / Next
                            │      ├─ LOSE → GameOverScreen → onRestart
                            │      └─ PAUSED ←─ onPause / onResume
```

`setupLevel(LevelDefinition)` всегда стартует в **BRIEFING** — игрок видит условия звёзд и жмёт `START`, что переводит в `PLAYING` (`BaseLevelViewModel.onStart()`).

### 4.3 LevelSystem-плагины

В пакете [`core/game/system/`](./app/src/main/java/com/cybermaze/core/game/system) лежат готовые механики. Подключаются через `LevelDefinition.extraSystems`:

| Система | Что делает | Нужен overlay-слой |
|---------|------------|---------------------|
| `PowerUpSystem` | Замедляет всех врагов на N сек после сбора `P` | нет (HUD рисует countdown) |
| `SpeedBoostSystem` | Хук для `S`-бустеров (таймер на самом `Player`) | нет |
| `TeleportSystem` | Парные телепорты `A` ↔ `B` с кулдауном | `TeleportPulseLayer` |
| `MovingTrapSystem` | Подвижные шипы по `path` | `MovingTrapsLayer` |
| `GuardSystem` | AI охранника + конус видимости | `GuardVisionLayer` |
| `FogOfWarSystem` | Туман войны + вижн-буст от `P` | `FogOfWarLayer` |

Полный пример как «собрать уровень из лего» — `features/level_template/LevelTemplateViewModel.kt`.

---

## 5. Как переиспользовать и менять компоненты

### 5.1 Сделать новый уровень

1. Скопировать `features/level_template/` в `features/levelXX/`.
2. Переименовать `LevelTemplate*` → `LevelXX*` и package.
3. Отредактировать `LevelXXMap.kt`, `LevelXXConfig.kt`, наборы врагов / ловушек / телепортов.
4. В `LevelXXViewModel.setupLevel(LevelDefinition(...))` оставить только те `extraSystems`, которые нужны уровню.
5. Подключить экран в [`core/navigation/NavGraph.kt`](./app/src/main/java/com/cybermaze/core/navigation/NavGraph.kt).

Подробно — `docs/LEVEL_DEVELOPER_GUIDE.md`.

### 5.2 Перекрасить уровень

`LevelPalette` собрана так, что меняешь только нужные поля, остальные наследуются из `CyberColors`:

```kotlin
val MY_PALETTE = LevelPalette(
    background = Color(0xFF0F0028),
    player     = Color(0xFFE5FF00),
    enemyChaser = Color(0xFFFF0066)
)
setupLevel(LevelDefinition(..., palette = MY_PALETTE))
```

Все рендереры (`GameCanvas`, overlay-слои, `GameHUD`) читают цвета из `gameState.palette` автоматически.

### 5.3 Кастомный фон

```kotlin
Box(Modifier.fillMaxSize().background(state.palette.background)) {
    LevelBackgroundLayer(palette = state.palette, style = LevelBackgroundStyle.Radial)
    GameCanvas(gameState = state, drawBackground = false)
    // overlay-слои + HUD как обычно
}
```

`LevelBackgroundLayer` имеет три стиля (`Grid`, `Radial`, `ScanLines`) и перегрузку для произвольного `@Composable`.

### 5.4 Поменять скорость игрока / врагов

- Глобальная: [`Player.DEFAULT_MOVE_INTERVAL`](./app/src/main/java/com/cybermaze/core/game/model/Player.kt) и `Player.speedMultiplier`.
- Для конкретного уровня: создавай `Player` с нужным `moveInterval` и `speedMultiplier` в `createInitialState()` или передавай через `Enemy.speed` для врагов.
- Бустер `S` живёт в `Player.speedBoostSecondsLeft` и сам сбрасывается через `advanceTimers`.

### 5.5 Добавить свою механику

1. Создаёшь объект / класс, реализующий `LevelSystem`.
2. Добавляешь его в `LevelDefinition.extraSystems`.
3. При необходимости расширяешь `GameState` (новое поле + `updateXxx()` метод) и пишешь optional overlay-композабл в `core/ui/renderer/`.

### 5.6 Условия трёх звёзд

Универсальные правила в [`core/game/level/LevelResult.kt`](./app/src/main/java/com/cybermaze/core/game/level/LevelResult.kt):

| Звезда | Условие |
|--------|---------|
| ★ 1 | Уровень завершён (`collectedPoints ≥ requiredPoints`, доехал до `E`) |
| ★ 2 | `time ≤ timeLimit × 0.75` (требует не-null `timeLimit`) |
| ★ 3 | Собраны **все** энергетические точки **и** не потеряно жизней |

Обе экранные подсказки уже подключены:

- До игры — `LevelBriefingScreen` (показывается на фазе `BRIEFING`).
- После игры — `LevelCompleteScreen` (✓/✗ для каждого условия + пояснение промахов).

Подробности и пример подключения в новом уровне — раздел 5 `LEVEL_DEVELOPER_GUIDE.md`.

---

## 6. FAQ / типовые задачи

| Хочу… | Куда смотреть |
|-------|---------------|
| Понять что должно быть в финальной игре | `CyberMaze_TZ.md` |
| Добавить свой уровень | `docs/LEVEL_DEVELOPER_GUIDE.md` + копия `features/level_template/` |
| Изменить цвета / стиль уровня | `LevelPalette` + поле `palette` в `LevelDefinition` |
| Поменять скорость игрока | `Player.kt` (поля `moveInterval`, `speedMultiplier`) |
| Поменять что показывает HUD | `core/ui/components/GameHUD.kt` |
| Добавить новый коллектибл | `core/game/model/Collectible.kt` + `CollectibleSystem` + рендер в `GameCanvas` |
| Сделать так, чтобы враг не догонял | `Enemy.speedMultiplier` или `PowerUpSystem` |
| Сделать плавнее анимацию | Интерполяция уже есть в `GameCanvas` (`previousPosition` + `moveTimer`). Если нужно сильнее — уменьшай `moveInterval`. |
| Запустить сборку | `.\gradlew.bat :app:assembleDebug` |

---

## 7. Чек-лист перед коммитом в `main`

- [ ] `./gradlew :app:assembleDebug` зелёный.
- [ ] Не сломаны существующие уровни (Level 01 проходится).
- [ ] Если добавлен новый уровень — он прописан в `NavGraph` и `LevelSelectViewModel`.
- [ ] Палитра / фон / системы корректно сбрасываются при `onRestart` (см. `createInitialState`).
- [ ] Обновлён `LEVEL_DEVELOPER_GUIDE.md`, если ввёл новые публичные API.
