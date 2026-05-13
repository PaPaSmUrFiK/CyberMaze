# 🎮 CYBER MAZE — Полное Техническое Задание
### Командный студенческий проект | Kotlin + Jetpack Compose | 10 разработчиков

---

## СОДЕРЖАНИЕ

1. [Общее техническое задание](#1-общее-техническое-задание)
2. [Полная архитектура проекта](#2-полная-архитектура-проекта)
3. [Общая игровая инфраструктура](#3-общая-игровая-инфраструктура)
4. [Database Design (Room)](#4-database-design-room)
5. [Распределение на 10 человек](#5-распределение-на-10-человек)
6. [Подробное ТЗ каждого уровня](#6-подробное-тз-каждого-уровня)
7. [Прогрессия сложности](#7-прогрессия-сложности)
8. [Интеграция проекта](#8-интеграция-проекта)
9. [UI/UX Style Guide](#9-uiux-style-guide)
10. [Roadmap](#10-roadmap)

---

# 1. Общее техническое задание

## 1.1 Концепция игры

**Название:** Cyber Maze  
**Жанр:** 2D grid-based arcade maze game (Pac-Man-like)  
**Платформа:** Android (min SDK 26, target SDK 34)  
**Ориентация экрана:** Landscape (горизонтальная) — зафиксирована, поворот не поддерживается  
**Сеттинг:** Киберпанк / цифровой лабиринт / neon cyber world

**Описание:**  
Игрок управляет киберагентом, который перемещается по лабиринту цифровой матрицы. Цель — собрать все точки данных (energy points), избежать вражеских программ-охранников и найти выход из каждого уровня.

## 1.2 Gameplay

### Основные действия игрока:
- Перемещение по grid-карте (вверх / вниз / влево / вправо)
- Сбор energy points (основные очки)
- Сбор ключей для открытия дверей
- Избегание врагов
- Использование бонусов (Power-Up, Speed Boost, Shield)
- Активация телепортов (на поздних уровнях)

### Управление:
- Свайп по экрану (основное)
- D-pad кнопки **поверх игровой области** (полупрозрачный overlay, нижний левый угол)

### Условия победы:
- Собрать все required collectibles на уровне
- Добраться до выхода (Exit tile)

### Условия поражения:
- Контакт с врагом (при отсутствии щита)
- Истечение таймера (на уровнях с лимитом времени)

## 1.3 Progression System

```
Уровень разблокируется только после прохождения предыдущего.

Система звёзд за каждый уровень:
⭐        — уровень пройден
⭐⭐      — пройден быстро (в 75% от лимита времени)
⭐⭐⭐    — пройден идеально (собраны все предметы, без потери жизней)

Глобальная статистика:
- Общий счёт по всем уровням
- Лучшее время на каждом уровне
- Количество собранных звёзд
```

## 1.4 Game Loop

```
┌─────────────────────────────────────────────────────┐
│                    GAME LOOP                        │
│                                                     │
│  1. Инициализация уровня (LevelLoader)              │
│  2. Отрисовка карты (Canvas Renderer)               │
│  3. Обработка ввода (Input Handler)                 │
│  4. Обновление состояния:                           │
│     - Движение игрока                               │
│     - Движение врагов                               │
│     - Коллизии                                      │
│     - Сбор предметов                                │
│  5. Проверка win/lose условий                       │
│  6. Отрисовка следующего кадра                      │
│  7. Сохранение прогресса (если нужно)               │
│  → Goto 3 (60 FPS target)                           │
└─────────────────────────────────────────────────────┘
```

## 1.5 Основные экраны

| Экран | Описание |
|-------|----------|
| SplashScreen | Логотип + загрузка |
| MainMenuScreen | Play, Settings, Leaderboard, About |
| LevelSelectScreen | Сетка уровней, звёзды, lock/unlock |
| GameScreen | Основной игровой экран |
| PauseMenuScreen | Resume, Restart, Main Menu |
| LevelCompleteScreen | Счёт, звёзды, Next Level |
| GameOverScreen | Счёт, Retry, Main Menu |
| SettingsScreen | Звук, вибрация, управление |
| LeaderboardScreen | Рекорды по уровням |

---

# 2. Полная архитектура проекта

## 2.1 Структура папок

```
CyberMaze/
├── app/
│   ├── src/main/
│   │   ├── AndroidManifest.xml
│   │   └── kotlin/com/cybermaze/
│   │       ├── CyberMazeApp.kt              ← Hilt Application
│   │       └── MainActivity.kt
│
├── core/
│   ├── game/                                ← Игровой движок
│   │   ├── engine/
│   │   │   ├── GameLoop.kt
│   │   │   ├── GameState.kt
│   │   │   └── GameEvent.kt
│   │   ├── model/
│   │   │   ├── Player.kt
│   │   │   ├── Enemy.kt
│   │   │   ├── Tile.kt
│   │   │   ├── GameMap.kt
│   │   │   ├── Position.kt
│   │   │   ├── Direction.kt
│   │   │   └── Collectible.kt
│   │   ├── system/
│   │   │   ├── MovementSystem.kt
│   │   │   ├── CollisionSystem.kt
│   │   │   ├── EnemySystem.kt
│   │   │   └── CollectibleSystem.kt
│   │   ├── level/
│   │   │   ├── LevelConfig.kt
│   │   │   ├── LevelLoader.kt
│   │   │   └── LevelResult.kt
│   │   └── save/
│   │       ├── SaveManager.kt
│   │       └── GameProgress.kt
│   │
│   ├── ui/                                  ← Shared UI
│   │   ├── theme/
│   │   │   ├── CyberTheme.kt
│   │   │   ├── Colors.kt
│   │   │   └── Typography.kt
│   │   ├── components/
│   │   │   ├── GameHUD.kt
│   │   │   ├── PauseMenu.kt
│   │   │   ├── GameOverScreen.kt
│   │   │   ├── LevelCompleteScreen.kt
│   │   │   ├── DPadControl.kt
│   │   │   └── NeonButton.kt
│   │   └── renderer/
│   │       ├── GameRenderer.kt              ← Canvas rendering
│   │       ├── TileRenderer.kt
│   │       ├── PlayerRenderer.kt
│   │       └── EnemyRenderer.kt
│   │
│   ├── navigation/
│   │   ├── NavGraph.kt
│   │   └── Screen.kt
│   │
│   ├── database/
│   │   ├── CyberMazeDatabase.kt
│   │   ├── entity/
│   │   │   ├── PlayerProgressEntity.kt
│   │   │   ├── LevelRecordEntity.kt
│   │   │   └── SettingsEntity.kt
│   │   ├── dao/
│   │   │   ├── PlayerProgressDao.kt
│   │   │   ├── LevelRecordDao.kt
│   │   │   └── SettingsDao.kt
│   │   └── repository/
│   │       ├── ProgressRepository.kt
│   │       └── SettingsRepository.kt
│   │
│   └── utils/
│       ├── Constants.kt
│       ├── Extensions.kt
│       └── Logger.kt
│
└── features/
    ├── level_01/
    │   ├── Level01Config.kt
    │   ├── Level01ViewModel.kt
    │   ├── Level01Screen.kt
    │   └── Level01Map.kt
    ├── level_02/
    │   └── ...
    ├── level_03/ → level_10/
    │   └── ...
    ├── menu/
    │   ├── MainMenuScreen.kt
    │   └── MainMenuViewModel.kt
    ├── level_select/
    │   ├── LevelSelectScreen.kt
    │   └── LevelSelectViewModel.kt
    └── settings/
        ├── SettingsScreen.kt
        └── SettingsViewModel.kt
```

## 2.2 Архитектура MVVM

```
Screen (Composable)
    ↕ observes State
ViewModel
    ↕ uses
Repository
    ↕ uses
Room DAO / Game Systems
```

## 2.3 Shared Interfaces

```kotlin
// Каждый уровень реализует этот интерфейс
interface LevelFeature {
    val levelNumber: Int
    val config: LevelConfig
    fun createViewModel(): BaseLevelViewModel
    fun createScreen(): @Composable () -> Unit
}

// Базовый ViewModel для всех уровней
abstract class BaseLevelViewModel(
    protected val progressRepository: ProgressRepository
) : ViewModel() {
    abstract val gameState: StateFlow<GameState>
    abstract fun onDirectionInput(direction: Direction)
    abstract fun onPause()
    abstract fun onResume()
    abstract fun onRestart()
}
```

---

# 3. Общая игровая инфраструктура

## 3.1 Модели данных

```kotlin
// Position на grid
data class Position(val x: Int, val y: Int) {
    fun moved(direction: Direction): Position = when (direction) {
        Direction.UP    -> copy(y = y - 1)
        Direction.DOWN  -> copy(y = y + 1)
        Direction.LEFT  -> copy(x = x - 1)
        Direction.RIGHT -> copy(x = x + 1)
    }
    operator fun plus(other: Position) = Position(x + other.x, y + other.y)
}

// Направление
enum class Direction { UP, DOWN, LEFT, RIGHT, NONE }

// Тип тайла
enum class TileType {
    EMPTY,           // Пустая клетка (можно ходить)
    WALL,            // Стена (нельзя ходить)
    ENERGY_POINT,    // Точка энергии (собирается)
    DOOR,            // Дверь (открывается ключом)
    KEY,             // Ключ
    EXIT,            // Выход
    TELEPORT_A,      // Телепорт A
    TELEPORT_B,      // Телепорт B
    TRAP,            // Ловушка
    POWER_UP,        // Усиление
    SPEED_BOOST,     // Ускорение
    SHIELD,          // Щит
    DARKNESS_ZONE,   // Зона темноты
    SPAWN_POINT      // Точка спавна игрока
}

// Тайл
data class Tile(
    val type: TileType,
    val position: Position,
    val isPassable: Boolean = type != TileType.WALL && type != TileType.DOOR,
    val metadata: Map<String, Any> = emptyMap()
)

// Карта уровня
data class GameMap(
    val width: Int,
    val height: Int,
    val tiles: Array<Array<Tile>>,
    val playerSpawn: Position,
    val exitPosition: Position
) {
    fun getTile(pos: Position): Tile? =
        tiles.getOrNull(pos.y)?.getOrNull(pos.x)

    fun isPassable(pos: Position): Boolean =
        getTile(pos)?.isPassable == true

    fun isInBounds(pos: Position): Boolean =
        pos.x in 0 until width && pos.y in 0 until height
}

// Игрок
data class Player(
    val position: Position,
    val lives: Int = 3,
    val score: Int = 0,
    val hasShield: Boolean = false,
    val speedMultiplier: Float = 1.0f,
    val keysCollected: Int = 0,
    val direction: Direction = Direction.NONE
)

// Враг
data class Enemy(
    val id: String,
    val position: Position,
    val type: EnemyType,
    val speed: Float = 1.0f,
    val direction: Direction = Direction.RIGHT,
    val isActive: Boolean = true,
    val patrolPath: List<Position> = emptyList()
)

enum class EnemyType {
    PATROL,      // Ходит по маршруту
    CHASER,      // Преследует игрока
    RANDOM,      // Двигается случайно
    GUARD,       // Стоит на месте, смотрит в сторону
    FAST         // Быстрый, случайный
}

// Конфигурация уровня
data class LevelConfig(
    val levelNumber: Int,
    val title: String,
    val description: String,
    val timeLimit: Int? = null,         // в секундах, null = без лимита
    val requiredPoints: Int,            // нужно собрать для победы
    val totalPoints: Int,               // всего точек на карте
    val hasKeys: Boolean = false,
    val hasTeleports: Boolean = false,
    val hasDarkness: Boolean = false,
    val hasMovingTraps: Boolean = false,
    val specialMechanic: String? = null
)

// Состояние игры
data class GameState(
    val player: Player,
    val enemies: List<Enemy>,
    val map: GameMap,
    val config: LevelConfig,
    val collectedPoints: Int = 0,
    val elapsedTime: Float = 0f,
    val phase: GamePhase = GamePhase.PLAYING,
    val score: Int = 0
)

enum class GamePhase {
    PLAYING, PAUSED, WIN, LOSE, LOADING
}

// Коллектибл
data class Collectible(
    val position: Position,
    val type: CollectibleType,
    val value: Int = 10,
    val isCollected: Boolean = false
)

enum class CollectibleType {
    ENERGY_POINT, KEY, POWER_UP, SPEED_BOOST, SHIELD, BONUS_STAR
}
```

## 3.2 Game Loop

```kotlin
class GameLoop(
    private val onUpdate: (deltaTime: Float) -> Unit,
    private val onRender: () -> Unit
) {
    private var isRunning = false
    private var lastFrameTime = 0L

    fun start() {
        isRunning = true
        // Запускается в coroutine scope через LaunchedEffect
    }

    fun stop() { isRunning = false }
    fun pause() { isRunning = false }
    fun resume() { isRunning = true }

    // Вызывается из LaunchedEffect в Composable
    suspend fun tick() {
        val currentTime = System.nanoTime()
        val deltaTime = (currentTime - lastFrameTime) / 1_000_000_000f
        lastFrameTime = currentTime
        if (isRunning) {
            onUpdate(deltaTime.coerceAtMost(0.1f)) // cap delta
            onRender()
        }
    }
}

// Использование в Composable:
// LaunchedEffect(Unit) {
//     while(true) {
//         gameLoop.tick()
//         delay(16L) // ~60 FPS
//     }
// }
```

## 3.3 Movement System

```kotlin
class MovementSystem {
    fun canMove(pos: Position, direction: Direction, map: GameMap): Boolean {
        val newPos = pos.moved(direction)
        return map.isInBounds(newPos) && map.isPassable(newPos)
    }

    fun movePlayer(player: Player, direction: Direction, map: GameMap): Player {
        if (direction == Direction.NONE) return player
        if (!canMove(player.position, direction, map)) return player
        return player.copy(
            position = player.position.moved(direction),
            direction = direction
        )
    }

    fun moveEnemy(enemy: Enemy, map: GameMap, playerPos: Position): Enemy {
        return when (enemy.type) {
            EnemyType.PATROL  -> movePatrol(enemy, map)
            EnemyType.CHASER  -> moveChaser(enemy, map, playerPos)
            EnemyType.RANDOM  -> moveRandom(enemy, map)
            EnemyType.GUARD   -> enemy  // не двигается
            EnemyType.FAST    -> moveRandom(enemy, map)
        }
    }

    private fun movePatrol(enemy: Enemy, map: GameMap): Enemy {
        if (enemy.patrolPath.isEmpty()) return enemy
        val currentIndex = enemy.patrolPath.indexOf(enemy.position)
        val nextIndex = (currentIndex + 1) % enemy.patrolPath.size
        return enemy.copy(position = enemy.patrolPath[nextIndex])
    }

    private fun moveChaser(enemy: Enemy, map: GameMap, target: Position): Enemy {
        // Простое приближение к цели (без BFS, просто по наибольшей разнице)
        val dx = target.x - enemy.position.x
        val dy = target.y - enemy.position.y
        val preferredDir = if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0) Direction.RIGHT else Direction.LEFT
        } else {
            if (dy > 0) Direction.DOWN else Direction.UP
        }
        return if (canMove(enemy.position, preferredDir, map)) {
            enemy.copy(position = enemy.position.moved(preferredDir), direction = preferredDir)
        } else {
            moveRandom(enemy, map)
        }
    }

    private fun moveRandom(enemy: Enemy, map: GameMap): Enemy {
        val dirs = Direction.entries.filter { it != Direction.NONE }
            .filter { canMove(enemy.position, it, map) }
        if (dirs.isEmpty()) return enemy
        val dir = dirs.random()
        return enemy.copy(position = enemy.position.moved(dir), direction = dir)
    }
}
```

## 3.4 Collision System

```kotlin
class CollisionSystem {
    fun checkPlayerEnemyCollision(player: Player, enemies: List<Enemy>): Boolean {
        return enemies.any { it.position == player.position && it.isActive }
    }

    fun checkPlayerCollectible(
        player: Player,
        collectibles: List<Collectible>
    ): List<Collectible> {
        return collectibles.filter {
            it.position == player.position && !it.isCollected
        }
    }

    fun checkPlayerTile(player: Player, map: GameMap): TileType? {
        return map.getTile(player.position)?.type
    }

    fun handleCollision(
        player: Player,
        enemies: List<Enemy>
    ): Player {
        return if (player.hasShield) {
            player.copy(hasShield = false) // щит поглощает удар
        } else {
            player.copy(lives = player.lives - 1)
        }
    }
}
```

## 3.5 Level Loader

```kotlin
// Карта задаётся строками для удобства студентов
object LevelLoader {
    fun loadFromStringArray(
        layout: Array<String>,
        config: LevelConfig
    ): GameMap {
        val height = layout.size
        val width = layout[0].length
        var playerSpawn = Position(0, 0)
        var exitPos = Position(width - 1, height - 1)

        val tiles = Array(height) { y ->
            Array(width) { x ->
                val char = layout[y].getOrNull(x) ?: '.'
                val tileType = charToTileType(char)
                if (tileType == TileType.SPAWN_POINT) playerSpawn = Position(x, y)
                if (tileType == TileType.EXIT) exitPos = Position(x, y)
                Tile(type = tileType, position = Position(x, y))
            }
        }
        return GameMap(width, height, tiles, playerSpawn, exitPos)
    }

    private fun charToTileType(char: Char): TileType = when (char) {
        '#'  -> TileType.WALL
        '.'  -> TileType.EMPTY
        'o'  -> TileType.ENERGY_POINT
        'D'  -> TileType.DOOR
        'K'  -> TileType.KEY
        'E'  -> TileType.EXIT
        'A'  -> TileType.TELEPORT_A
        'B'  -> TileType.TELEPORT_B
        'T'  -> TileType.TRAP
        'P'  -> TileType.POWER_UP
        'S'  -> TileType.SPEED_BOOST
        'H'  -> TileType.SHIELD
        'X'  -> TileType.SPAWN_POINT
        else -> TileType.EMPTY
    }
}
```

## 3.6 Canvas Renderer

```kotlin
@Composable
fun GameCanvas(
    gameState: GameState,
    modifier: Modifier = Modifier
) {
    val tileSizePx = remember { mutableStateOf(0f) }

    Canvas(modifier = modifier.fillMaxSize()) {
        val mapWidth = gameState.map.width
        val mapHeight = gameState.map.height
        val tileSize = minOf(size.width / mapWidth, size.height / mapHeight)
        tileSizePx.value = tileSize

        // 1. Фон
        drawRect(color = Color(0xFF0A0A1A))

        // 2. Тайлы
        gameState.map.tiles.forEach { row ->
            row.forEach { tile ->
                drawTile(tile, tileSize)
            }
        }

        // 3. Коллектиблы (energy points и т.д.)
        // drawCollectibles(...)

        // 4. Враги
        gameState.enemies.forEach { enemy ->
            drawEnemy(enemy, tileSize)
        }

        // 5. Игрок
        drawPlayer(gameState.player, tileSize)
    }
}

private fun DrawScope.drawTile(tile: Tile, tileSize: Float) {
    val x = tile.position.x * tileSize
    val y = tile.position.y * tileSize
    val color = when (tile.type) {
        TileType.WALL         -> Color(0xFF1A3A5C)
        TileType.EXIT         -> Color(0xFF00FF88)
        TileType.DOOR         -> Color(0xFFFF6600)
        TileType.KEY          -> Color(0xFFFFD700)
        TileType.ENERGY_POINT -> Color(0xFF00BFFF)
        TileType.TELEPORT_A   -> Color(0xFFAA00FF)
        TileType.TELEPORT_B   -> Color(0xFFFF00AA)
        TileType.SHIELD       -> Color(0xFF00FFFF)
        TileType.TRAP         -> Color(0xFFFF0000)
        else -> Color.Transparent
    }
    if (color != Color.Transparent) {
        drawRect(color = color, topLeft = Offset(x, y), size = Size(tileSize, tileSize))
    }
}

private fun DrawScope.drawPlayer(player: Player, tileSize: Float) {
    val cx = player.position.x * tileSize + tileSize / 2
    val cy = player.position.y * tileSize + tileSize / 2
    drawCircle(color = Color(0xFF00FF88), radius = tileSize * 0.4f, center = Offset(cx, cy))
    // Glow effect
    drawCircle(
        color = Color(0x4400FF88),
        radius = tileSize * 0.55f,
        center = Offset(cx, cy)
    )
}

private fun DrawScope.drawEnemy(enemy: Enemy, tileSize: Float) {
    val cx = enemy.position.x * tileSize + tileSize / 2
    val cy = enemy.position.y * tileSize + tileSize / 2
    val color = when (enemy.type) {
        EnemyType.CHASER -> Color(0xFFFF3030)
        EnemyType.PATROL -> Color(0xFFFF8C00)
        EnemyType.FAST   -> Color(0xFFFF00FF)
        else -> Color(0xFFFF6060)
    }
    drawRect(
        color = color,
        topLeft = Offset(enemy.position.x * tileSize + tileSize * 0.1f,
                         enemy.position.y * tileSize + tileSize * 0.1f),
        size = Size(tileSize * 0.8f, tileSize * 0.8f)
    )
}
```

---

# 4. Database Design (Room)

## 4.1 Entities

```kotlin
// Прогресс игрока по уровням
@Entity(tableName = "player_progress")
data class PlayerProgressEntity(
    @PrimaryKey val levelNumber: Int,
    val isUnlocked: Boolean = false,
    val isCompleted: Boolean = false,
    val starsEarned: Int = 0,            // 0-3
    val bestScore: Int = 0,
    val bestTimeSeconds: Float = 0f,
    val totalAttempts: Int = 0,
    val lastPlayedAt: Long = 0L          // timestamp
)

// Рекорды по уровням
@Entity(tableName = "level_records")
data class LevelRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val levelNumber: Int,
    val score: Int,
    val timeSeconds: Float,
    val starsEarned: Int,
    val achievedAt: Long = System.currentTimeMillis()
)

// Настройки
@Entity(tableName = "settings")
data class SettingsEntity(
    @PrimaryKey val id: Int = 1,         // Singleton
    val soundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val controlType: String = "SWIPE",   // SWIPE | DPAD
    val musicVolume: Float = 0.7f,
    val sfxVolume: Float = 1.0f
)
```

## 4.2 DAOs

```kotlin
@Dao
interface PlayerProgressDao {
    @Query("SELECT * FROM player_progress ORDER BY levelNumber")
    fun getAllProgress(): Flow<List<PlayerProgressEntity>>

    @Query("SELECT * FROM player_progress WHERE levelNumber = :level")
    suspend fun getProgressForLevel(level: Int): PlayerProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProgress(progress: PlayerProgressEntity)

    @Query("UPDATE player_progress SET isUnlocked = 1 WHERE levelNumber = :level")
    suspend fun unlockLevel(level: Int)

    @Query("SELECT SUM(bestScore) FROM player_progress")
    fun getTotalScore(): Flow<Int?>
}

@Dao
interface LevelRecordDao {
    @Query("SELECT * FROM level_records WHERE levelNumber = :level ORDER BY score DESC LIMIT 10")
    fun getTopRecordsForLevel(level: Int): Flow<List<LevelRecordEntity>>

    @Query("SELECT * FROM level_records ORDER BY score DESC LIMIT 20")
    fun getGlobalLeaderboard(): Flow<List<LevelRecordEntity>>

    @Insert
    suspend fun insertRecord(record: LevelRecordEntity)
}

@Dao
interface SettingsDao {
    @Query("SELECT * FROM settings WHERE id = 1")
    fun getSettings(): Flow<SettingsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSettings(settings: SettingsEntity)
}
```

## 4.3 Database & DI

```kotlin
@Database(
    entities = [PlayerProgressEntity::class, LevelRecordEntity::class, SettingsEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CyberMazeDatabase : RoomDatabase() {
    abstract fun playerProgressDao(): PlayerProgressDao
    abstract fun levelRecordDao(): LevelRecordDao
    abstract fun settingsDao(): SettingsDao
}

// Hilt Module
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CyberMazeDatabase =
        Room.databaseBuilder(context, CyberMazeDatabase::class.java, "cybermaze.db").build()

    @Provides fun provideProgressDao(db: CyberMazeDatabase) = db.playerProgressDao()
    @Provides fun provideRecordDao(db: CyberMazeDatabase) = db.levelRecordDao()
    @Provides fun provideSettingsDao(db: CyberMazeDatabase) = db.settingsDao()
}
```

## 4.4 Repository

```kotlin
class ProgressRepository @Inject constructor(
    private val progressDao: PlayerProgressDao,
    private val recordDao: LevelRecordDao
) {
    fun getAllProgress(): Flow<List<PlayerProgressEntity>> = progressDao.getAllProgress()

    suspend fun completeLevel(
        levelNumber: Int,
        score: Int,
        timeSeconds: Float,
        stars: Int
    ) {
        val existing = progressDao.getProgressForLevel(levelNumber)
        val updated = PlayerProgressEntity(
            levelNumber = levelNumber,
            isUnlocked = true,
            isCompleted = true,
            starsEarned = maxOf(existing?.starsEarned ?: 0, stars),
            bestScore = maxOf(existing?.bestScore ?: 0, score),
            bestTimeSeconds = if (existing?.bestTimeSeconds == 0f) timeSeconds
                             else minOf(existing?.bestTimeSeconds ?: timeSeconds, timeSeconds),
            totalAttempts = (existing?.totalAttempts ?: 0) + 1,
            lastPlayedAt = System.currentTimeMillis()
        )
        progressDao.upsertProgress(updated)
        recordDao.insertRecord(LevelRecordEntity(levelNumber = levelNumber, score = score,
                                                 timeSeconds = timeSeconds, starsEarned = stars))
        // Разблокировать следующий уровень
        if (levelNumber < 10) progressDao.unlockLevel(levelNumber + 1)
    }
}
```

---

# 5. Распределение на 10 человек

## Таблица уровней

| # | Студент | Название уровня | Главная механика | Сложность |
|---|---------|-----------------|------------------|-----------|
| 1 | Dev 1 | Boot Sector | Обучение, базовое движение | ⭐ |
| 2 | Dev 2 | Data Harvest | Больше врагов, стратегия | ⭐⭐ |
| 3 | Dev 3 | Speed Protocol | Быстрые враги, таймер | ⭐⭐ |
| 4 | Dev 4 | Locked Grid | Ключи и двери | ⭐⭐⭐ |
| 5 | Dev 5 | Trap Matrix | Движущиеся ловушки | ⭐⭐⭐ |
| 6 | Dev 6 | Warp Zone | Система телепортов | ⭐⭐⭐ |
| 7 | Dev 7 | Blackout | Темнота / туман войны | ⭐⭐⭐⭐ |
| 8 | Dev 8 | Hunter Pack | Смешанные типы врагов | ⭐⭐⭐⭐ |
| 9 | Dev 9 | Deep Maze | Сложный лабиринт | ⭐⭐⭐⭐ |
| 10 | Dev 10 | Final Core | Финальный challenge | ⭐⭐⭐⭐⭐ |

## Общие обязательства каждого студента

Каждый студент создаёт в своём package `features/level_XX/`:

| Файл | Описание |
|------|----------|
| `LevelXXScreen.kt` | Composable экран уровня |
| `LevelXXViewModel.kt` | ViewModel, extends BaseLevelViewModel |
| `LevelXXMap.kt` | Карта в виде Array<String> |
| `LevelXXConfig.kt` | LevelConfig для этого уровня |
| `LevelXXEnemies.kt` | Список врагов с их маршрутами |

---

# 6. Подробное ТЗ каждого уровня

---

## УРОВЕНЬ 1 — "Boot Sector"
**Разработчик:** Dev 1 | **Сложность:** ⭐

### Концепция
Обучающий уровень. Игрок впервые попадает в Cyber Maze. Простая карта, один враг, много энергии. Никаких сложных механик.

### Карта (27×11) — landscape формат
```
###########################
#Xoo.ooo..ooo..ooo..ooo..#
#.#.#####.###.#####.###..#
#.#.#...#.#.#.#...#.#.o..#
#.#.#.o.#...#.#.o.#...#..#
#...#...#.o.#...#...o.#..#
#.###.###...###.###...#..#
#.#...#.#.###.#.#.#.o.#..#
#.#.o.#...#...#...#...#..#
#ooo..o.o.o.o.o.o.oooooE.#
###########################
```
_(X = спавн игрока, E = выход, o = energy point)_

### Враги
| ID | Тип | Маршрут | Скорость |
|----|-----|---------|----------|
| enemy_01 | PATROL | [(7,1)→(7,8)→(7,1)] | 0.5x |

### Механики
- Базовое движение
- Сбор energy points
- Один медленный patrol враг

### Win условие
- Собрать минимум 80% energy points
- Дойти до Exit

### Lose условие
- Контакт с врагом (3 жизни)

### Что реализует студент
```kotlin
// Level01Map.kt
val LEVEL_01_MAP = arrayOf(
    "###########################",
    "#Xoo.ooo..ooo..ooo..ooo..#",
    "#.#.#####.###.#####.###..#",
    "#.#.#...#.#.#.#...#.#.o..#",
    "#.#.#.o.#...#.#.o.#...#..#",
    "#...#...#.o.#...#...o.#..#",
    "#.###.###...###.###...#..#",
    "#.#...#.#.###.#.#.#.o.#..#",
    "#.#.o.#...#...#...#...#..#",
    "#ooo..o.o.o.o.o.o.oooooE.#",
    "###########################"
)

// Level01Config.kt
val LEVEL_01_CONFIG = LevelConfig(
    levelNumber = 1,
    title = "Boot Sector",
    description = "Initialize your systems, Agent.",
    timeLimit = null,
    requiredPoints = 80,   // % от total
    totalPoints = 20,
    hasKeys = false
)

// Level01Enemies.kt
val LEVEL_01_ENEMIES = listOf(
    Enemy(
        id = "patrol_01",
        position = Position(7, 1),
        type = EnemyType.PATROL,
        speed = 0.5f,
        patrolPath = listOf(Position(7,1), Position(7,3), Position(7,5), Position(7,3))
    )
)

// Level01ViewModel.kt — extends BaseLevelViewModel
// Level01Screen.kt — использует GameCanvas + GameHUD
```

### Shared классы, которые использует
- `GameCanvas`, `GameHUD`, `BaseLevelViewModel`
- `MovementSystem`, `CollisionSystem`
- `LevelLoader.loadFromStringArray()`
- `ProgressRepository`

### HUD
- Счёт (текущий)
- Жизни (3 сердечка)
- Прогресс сбора (X / Total)
- Кнопка паузы

---

## УРОВЕНЬ 2 — "Data Harvest"
**Разработчик:** Dev 2 | **Сложность:** ⭐⭐

### Концепция
Больше врагов, больше маршрутов. Игрок учится планировать пути и уклоняться.

### Карта (27×11) — landscape формат
```
###########################
#Xooo..P..ooo....ooo..P..#
#.###.###.###.##.###.###.#
#.#...#.#...#.##...#.#...#
#.#.###.#.###.######.#.###
#...#.......#........#...#
#.###.#.###.######.###.#.#
#.#...#.#.#....#...#...#.#
#.#.###.#.###.##.###.###.#
#ooo..o....oo....ooooooE.#
###########################
```
_(P = Power-Up, замедляет врагов)_

### Враги
| ID | Тип | Маршрут | Скорость |
|----|-----|---------|----------|
| patrol_01 | PATROL | горизонтальный ряд 5 | 0.7x |
| patrol_02 | PATROL | вертикальный столбец 8 | 0.7x |
| random_01 | RANDOM | — | 0.6x |

### Новая механика: Power-Up
На карте размещены 2 Power-Up (`P`). При сборе враги замедляются на 5 секунд (speedMultiplier = 0.3f).

### Что реализует студент
- 3 врага с разными маршрутами
- Логику Power-Up (временное замедление врагов)
- Анимацию мигания врагов в режиме замедления

```kotlin
// В ViewModel:
private fun applyPowerUp() {
    _gameState.update { state ->
        state.copy(
            enemies = state.enemies.map { it.copy(speed = 0.3f) }
        )
    }
    viewModelScope.launch {
        delay(5000L)
        _gameState.update { state ->
            state.copy(enemies = state.enemies.map { it.copy(speed = originalSpeed) })
        }
    }
}
```

---

## УРОВЕНЬ 3 — "Speed Protocol"
**Разработчик:** Dev 3 | **Сложность:** ⭐⭐

### Концепция
Быстрые враги + таймер. Игрок должен успеть до истечения времени.

### Карта (27×11) — landscape формат
```
###########################
#Xoooooo...S...ooooooooo.#
#.######.#####.#########.#
#.#....#.#...#.#.......#.#
#.#.##.#.#.#.#.#.#####.#.#
#...##...#.#...#.#...#...#
#.####.###.#####.#.#.###.#
#.#....#...#.....#.#.#...#
#.#.####.###.#####.#.#.#.#
#ooo....S...oooooooooo.E.#
###########################
```
_(S = Speed Boost, прибавляет скорость игроку)_

### Враги
| ID | Тип | Скорость |
|----|-----|----------|
| fast_01 | FAST | 1.5x |
| fast_02 | FAST | 1.5x |
| fast_03 | FAST | 1.8x |

### Новая механика: Таймер + Speed Boost
- **Таймер:** 90 секунд
- **Speed Boost:** Собирая `S`, игрок получает 1.5x скорость на 3 секунды

### Что реализует студент
- CountDown таймер с отображением в HUD
- Speed Boost логику для игрока
- Lose condition по истечению таймера
- UI: таймер меняет цвет (зелёный → жёлтый → красный)

```kotlin
// В ViewModel:
private fun startTimer(limitSeconds: Int) {
    viewModelScope.launch {
        repeat(limitSeconds) { second ->
            delay(1000L)
            _timeRemaining.update { it - 1 }
            if (_timeRemaining.value <= 0) {
                _gameState.update { it.copy(phase = GamePhase.LOSE) }
                cancel()
            }
        }
    }
}
```

---

## УРОВЕНЬ 4 — "Locked Grid"
**Разработчик:** Dev 4 | **Сложность:** ⭐⭐⭐

### Концепция
Ключи и двери. Части карты заблокированы дверями, которые открываются при сборе ключей.

### Карта (29×11) — landscape формат
```
#############################
#Xoo..K..#oo..D..oo..K..oo.#
#.######.#.######.######.#.#
#.#....#.#.#....#.#....#.#.#
#.#.##.#...#.##.D.#.##.#...#
#...##.#.###.##.#.#.##.#.###
#.####.#.#...##...#....#.#.#
#.#K...#.#.####.#######.#.#.#
#.#.####.#.#..#.#.......#.#.#
#ooo....K..oo.D..oooooooooE.#
#############################
```
_(K = ключ, D = дверь — нужен ключ для прохода)_

### Механика ключей и дверей
- 4 ключа на карте
- 3 двери, каждая требует 1 ключ
- При сборе ключа `keysCollected++`
- При попытке пройти через дверь: если `keysCollected > 0` → дверь открывается, `keysCollected--`

### Что реализует студент
- `KeyDoorSystem.kt` — логика ключей и дверей
- Анимацию открытия двери (смена тайла DOOR → EMPTY)
- Счётчик ключей в HUD
- Визуальный эффект: дверь мигает при наличии ключа

```kotlin
// KeyDoorSystem.kt
class KeyDoorSystem {
    fun tryOpenDoor(player: Player, doorPos: Position, map: GameMap): Pair<Player, GameMap>? {
        if (player.keysCollected <= 0) return null
        val newPlayer = player.copy(keysCollected = player.keysCollected - 1)
        val newTiles = map.tiles.map { row ->
            row.map { tile ->
                if (tile.position == doorPos) tile.copy(type = TileType.EMPTY, isPassable = true)
                else tile
            }.toTypedArray()
        }.toTypedArray()
        return Pair(newPlayer, map.copy(tiles = newTiles))
    }
}
```

---

## УРОВЕНЬ 5 — "Trap Matrix"
**Разработчик:** Dev 5 | **Сложность:** ⭐⭐⭐

### Концепция
Движущиеся ловушки — объекты, которые перемещаются по заданному маршруту и убивают при контакте.

### Карта (29×11) — landscape формат
```
#############################
#Xooo....ooooooo....ooooo..#
#.#####.#########.#######..#
#.#...#.#.......#.#.....#..#
#.#.#.#.#.#####.#.#.###.#..#
#...#...#.#...#...#.#.#....#
#.###.###.#.#.#####.#.#.##.#
#.#...#...#.#.......#.#....#
#.#.###.###.#########.###..#
#ooo..o.o.oooooo.ooo.oooE..#
#############################
```
_(Ловушки двигаются по горизонтальным/вертикальным коридорам — маршруты задаются в коде)_

### Ловушки (MovingTrap)
```kotlin
data class MovingTrap(
    val id: String,
    val currentPosition: Position,
    val path: List<Position>,   // маршрут движения
    val pathIndex: Int = 0,
    val moveInterval: Float = 0.8f  // секунды между шагами
)
```

| ID | Маршрут | Интервал |
|----|---------|----------|
| trap_01 | горизонтально (y=3) | 0.8s |
| trap_02 | вертикально (x=7) | 1.0s |
| trap_03 | по периметру внутреннего квадрата | 1.2s |

### Что реализует студент
- `MovingTrapSystem.kt` — обновление позиций ловушек
- Отрисовку ловушек (красный ромб с пульсацией)
- Коллизии игрока с ловушками
- Анимацию ловушки (мигание предупреждение за 0.2s до движения)

```kotlin
class MovingTrapSystem {
    fun updateTraps(traps: List<MovingTrap>, deltaTime: Float): List<MovingTrap> {
        return traps.map { trap ->
            val newTimer = trap.moveTimer + deltaTime
            if (newTimer >= trap.moveInterval) {
                val nextIndex = (trap.pathIndex + 1) % trap.path.size
                trap.copy(
                    currentPosition = trap.path[nextIndex],
                    pathIndex = nextIndex,
                    moveTimer = 0f
                )
            } else {
                trap.copy(moveTimer = newTimer)
            }
        }
    }
}
```

---

## УРОВЕНЬ 6 — "Warp Zone"
**Разработчик:** Dev 6 | **Сложность:** ⭐⭐⭐

### Концепция
Система телепортов. На карте несколько пар телепортов: войдя в один, выходишь из другого.

### Карта (29×11) — landscape формат
```
#############################
#Xooo..A..ooo....ooo..B..oo#
#.####.#.#####.#.#####.#.##.#
#.#..#...#...#.#.#...#...#..#
#.#..#####.#.#.#.#.#.#####..#
#....#.....#...#...#.#......#
#.####.#####.#.#.###.#.####.#
#.#....#.....#.#...#...#....#
#.#.####.#####.#####.###.##.#
#ooo..B..ooo..ooooo..A..oooE#
#############################
```
_(A и B — пары телепортов: A1↔A2, B1↔B2)_

### Механика телепортов
- 2 пары телепортов (A1→A2, B1→B2)
- При наступлении на A1 игрок телепортируется на A2
- Кулдаун телепорта: 1.5 секунды (нельзя телепортироваться сразу обратно)
- Визуальный эффект: анимация "warp" при телепортации

```kotlin
data class TeleportPair(
    val id: String,
    val posA: Position,
    val posB: Position,
    val cooldownA: Float = 0f,
    val cooldownB: Float = 0f
)

class TeleportSystem {
    fun checkTeleport(
        player: Player,
        teleports: List<TeleportPair>,
        deltaTime: Float
    ): Pair<Player, List<TeleportPair>>? {
        teleports.forEach { tp ->
            if (player.position == tp.posA && tp.cooldownA <= 0f) {
                val newPlayer = player.copy(position = tp.posB)
                val updated = tp.copy(cooldownB = 1.5f)
                return Pair(newPlayer, teleports.map { if (it.id == tp.id) updated else it })
            }
            if (player.position == tp.posB && tp.cooldownB <= 0f) {
                val newPlayer = player.copy(position = tp.posA)
                val updated = tp.copy(cooldownA = 1.5f)
                return Pair(newPlayer, teleports.map { if (it.id == tp.id) updated else it })
            }
        }
        return null
    }
}
```

### Что реализует студент
- `TeleportSystem.kt`
- Визуальный эффект телепортации (flash + particle burst в Canvas)
- Индикатор кулдауна (пульсация телепорта)
- 2 пары телепортов с разными цветами

---

## УРОВЕНЬ 7 — "Blackout"
**Разработчик:** Dev 7 | **Сложность:** ⭐⭐⭐⭐

### Концепция
Туман войны. Игрок видит только ближайшие N тайлов вокруг себя. Карта скрыта во тьме.

### Карта (31×13) — landscape формат
```
###############################
#Xoo..ooo....ooo....ooo....oo.#
#.##.#####.#####.#####.#####..#
#....#...#.#...#.#...#.#...#..#
#.####.#.#.#.#.#.#.#.#.#.#.##.#
#.#....#...#.#...#.#...#.#.....#
#.#.########.#####.########.##.#
#...#.......#.....#.......#....#
#.###.#####.#.###.#.#####.###..#
#.#...#...#...#.#...#...#...#..#
#.#.###.#.#####.#####.#.###.##.#
#ooo..o.o..oo....oo..o.o..oooE.#
###############################
```
_(Весь экран в тумане — видна только область вокруг игрока радиусом 3 тайла)_

### Механика тумана войны
- Видимость: круг радиусом 3 тайла вокруг игрока
- Всё за пределами — тёмное (непрозрачный overlay)
- При сборе `P` (Power-Up) видимость расширяется до 5 тайлов на 5 секунд

```kotlin
// В GameCanvas:
private fun DrawScope.drawFogOfWar(
    playerPos: Position,
    tileSize: Float,
    visibilityRadius: Int
) {
    val playerCx = playerPos.x * tileSize + tileSize / 2
    val playerCy = playerPos.y * tileSize + tileSize / 2

    // Тёмный overlay поверх всего
    drawRect(color = Color(0xDD000000), size = size)

    // "Вырезаем" круг видимости (через BlendMode)
    drawCircle(
        color = Color.Transparent,
        radius = visibilityRadius * tileSize,
        center = Offset(playerCx, playerCy),
        blendMode = BlendMode.Clear
    )
}
```

### Что реализует студент
- `FogOfWarSystem.kt` — управление видимостью
- Отрисовку тумана через Canvas BlendMode
- Градиентный переход по краям видимости
- `DarknessZone` тайлы — зоны с ещё более сильным туманом

```kotlin
class FogOfWarSystem {
    fun getVisiblePositions(
        center: Position,
        radius: Int,
        map: GameMap
    ): Set<Position> {
        val visible = mutableSetOf<Position>()
        for (dy in -radius..radius) {
            for (dx in -radius..radius) {
                val pos = Position(center.x + dx, center.y + dy)
                if (map.isInBounds(pos)) {
                    val dist = Math.sqrt((dx * dx + dy * dy).toDouble())
                    if (dist <= radius) visible.add(pos)
                }
            }
        }
        return visible
    }
}
```

---

## УРОВЕНЬ 8 — "Hunter Pack"
**Разработчик:** Dev 8 | **Сложность:** ⭐⭐⭐⭐

### Концепция
Смешанные типы врагов с разным поведением. Игрок должен одновременно следить за несколькими угрозами.

### Карта (31×11) — landscape формат
```
###############################
#Xooo..H..ooo....ooo..H..ooo.#
#.####.#.#####.#.#####.#.####.#
#.#..#...#...#.#.#...#...#..#.#
#.#..###.#.#.#.#.#.#.###.#..#.#
#....#...#.#...#...#...#.#...#.#
#.####.###.#.###.###.###.####.#
#.#....#...#.#...#...#.......#.#
#.#.####.###.#.###.###.#####.#.#
#ooo..ooo..oooooooooo..ooooooE.#
###############################
```
_(H = Shield, 6 врагов всех типов включая Guard у выхода)_

### Враги
| ID | Тип | Особенность |
|----|-----|-------------|
| chaser_01 | CHASER | Преследует игрока, 0.8x скорость |
| chaser_02 | CHASER | Преследует, 1.0x скорость |
| patrol_01 | PATROL | Горизонтальный маршрут |
| patrol_02 | PATROL | Вертикальный маршрут |
| fast_01 | FAST | Случайный, 1.6x скорость |
| guard_01 | GUARD | Стоит у выхода, периодически отворачивается |

### Новая механика: Guard (Страж)
- Страж стоит на месте и "смотрит" в одном направлении
- Если игрок в поле зрения (3 тайла перед ним) и Страж "смотрит" в его сторону — атакует
- Каждые 3 секунды Страж поворачивается

```kotlin
data class Guard(
    val position: Position,
    val lookDirection: Direction,
    val visionRange: Int = 3,
    val rotationInterval: Float = 3.0f,
    val rotationTimer: Float = 0f
) {
    fun isPlayerInSight(playerPos: Position): Boolean {
        return when (lookDirection) {
            Direction.RIGHT -> playerPos.y == position.y &&
                               playerPos.x in (position.x + 1)..(position.x + visionRange)
            Direction.LEFT  -> playerPos.y == position.y &&
                               playerPos.x in (position.x - visionRange)..(position.x - 1)
            Direction.DOWN  -> playerPos.x == position.x &&
                               playerPos.y in (position.y + 1)..(position.y + visionRange)
            Direction.UP    -> playerPos.x == position.x &&
                               playerPos.y in (position.y - visionRange)..(position.y - 1)
            else -> false
        }
    }
}
```

### Что реализует студент
- Guard механику с полем зрения
- Визуальный конус зрения (полупрозрачный треугольник)
- Координацию 6 врагов разных типов
- Shield логику (H тайл на карте)

---

## УРОВЕНЬ 9 — "Deep Maze"
**Разработчик:** Dev 9 | **Сложность:** ⭐⭐⭐⭐

### Концепция
Сложный лабиринт с множеством развилок и тупиков. Комбинация ключей, телепортов и врагов.

### Карта (33×13) — landscape формат
```
#################################
#Xoo.K.ooo..#..ooo.K.ooo..#..oo.#
#.##.#.###.###.###.#.###.###.##.#
#....#.#.#.#.#.#.#.#.#.#.#.#....#
#.####.#.#.#.#.#.#.#.#.#.#.#.##.#
#.#....#...A...#...B...#...#..#.#
#.#.##########.#.##########.##.#.#
#...#..........#..........#....#.#
#.###.########.#.########.###.##.#
#.#K..#......D.#.D......#..K#...#
#.#.###.######.#.######.###.#.#.#
#ooo..oooooo..ooooo..oooooooooE.#
#################################
```
_(K = ключ, D = дверь, A↔B = телепорт)_

### Враги
| ID | Тип | Особенность |
|----|-----|-------------|
| chaser_01 | CHASER | 1.0x |
| chaser_02 | CHASER | 0.9x |
| patrol_01..03 | PATROL | 3 маршрута |
| random_01..02 | RANDOM | 2 случайных |

### Что реализует студент
- Комбинацию всех ранее изученных механик
- Ключи + Двери (3 двери, 3 ключа)
- Телепорты (A ↔ B)
- 7 врагов разных типов

---

## УРОВЕНЬ 10 — "Final Core"
**Разработчик:** Dev 10 | **Сложность:** ⭐⭐⭐⭐⭐

### Концепция
Финальный boss-level. Все механики вместе. Таймер. Движущиеся ловушки. Туман. Много врагов.

### Карта (35×13) — landscape формат
```
###################################
#Xoo.K.oo..ooo....ooo..oo.K.ooo..#
#.##.#.##.#####.#.#####.##.#.###..#
#....#....#...#.#.#...#....#.#....#
#.####.##.#.#.#.#.#.#.#.##.####.#.#
#.#K...#..#.#.A.#.B.#.#..#....#.#.#
#.#.####.##.#.#####.#.##.####.#.#.#
#....#...#..#.......#..#...#..#...#
#.####.###.##.#####.##.###.#.####.#
#.#K...#...#D.#...#.D#...#.#.....#.#
#.#.####.###.##.#.##.###.####.##.#.#
#ooo..ooo..oooooooooooo..ooooooooE.#
###################################
```
_(Все механики: K=ключ, D=дверь, A↔B=телепорт, туман войны, движущиеся ловушки, 9 врагов)_

### Особые правила финального уровня
- **Таймер:** 180 секунд
- **Туман войны:** активен с начала
- **Движущиеся ловушки:** 4 штуки
- **Ключи:** 3 ключа, 3 двери
- **Телепорты:** 1 пара
- **Враги:** 9 врагов всех типов

### Финальная победная сцена
При прохождении уровня 10 — специальная анимация "система взломана":
```kotlin
@Composable
fun VictoryAnimation() {
    // Glitch эффект + текст "CORE BREACHED"
    // Счёт + все звёзды + congratulations
}
```

### Что реализует студент
- Все системы в одном уровне
- `FinalCoreSpecialEffects.kt` — глитч-эффект победы
- Boss-уровень должен быть самым красивым и сложным

---

# 7. Прогрессия сложности

```
Уровень │ Враги │ Таймер │ Ключи │ Телепорт │ Ловушки │ Туман │ Сложность
────────┼───────┼────────┼───────┼──────────┼─────────┼───────┼──────────
   1    │  1(P) │  Нет   │  Нет  │   Нет    │   Нет   │  Нет  │ ⭐
   2    │  3    │  Нет   │  Нет  │   Нет    │   Нет   │  Нет  │ ⭐⭐
   3    │  3(F) │  90s   │  Нет  │   Нет    │   Нет   │  Нет  │ ⭐⭐
   4    │  3    │  Нет   │  4/3  │   Нет    │   Нет   │  Нет  │ ⭐⭐⭐
   5    │  3    │  Нет   │  Нет  │   Нет    │   3     │  Нет  │ ⭐⭐⭐
   6    │  4    │  120s  │  Нет  │   2 пары │   Нет   │  Нет  │ ⭐⭐⭐
   7    │  4    │  Нет   │  Нет  │   Нет    │   Нет   │  Да   │ ⭐⭐⭐⭐
   8    │  6    │  150s  │  Нет  │   Нет    │   2     │  Нет  │ ⭐⭐⭐⭐
   9    │  7    │  Нет   │  3/3  │   1 пара │   2     │  Нет  │ ⭐⭐⭐⭐
  10    │  9    │  180s  │  3/3  │   1 пара │   4     │  Да   │ ⭐⭐⭐⭐⭐

P = Patrol, F = Fast
```

### Механики по уровням

| Механика | Вводится | Уровни |
|----------|----------|--------|
| Базовое движение | 1 | 1-10 |
| Power-Up (замедление) | 2 | 2-10 |
| Speed Boost | 3 | 3-10 |
| Таймер | 3 | 3, 6, 8, 10 |
| Ключи + Двери | 4 | 4, 9, 10 |
| Движущиеся ловушки | 5 | 5, 8, 9, 10 |
| Телепорты | 6 | 6, 9, 10 |
| Туман войны | 7 | 7, 10 |
| Guard (страж) | 8 | 8, 10 |
| Shield | 4 | 4-10 |

---

# 8. Интеграция проекта

## 8.1 Naming Conventions

```
Packages:    com.cybermaze.features.level_01
Classes:     Level01Screen, Level01ViewModel, Level01Config
Files:       Level01Screen.kt, Level01Map.kt
Resources:   level_01_theme_color, ic_level_01
Constants:   LEVEL_01_TIME_LIMIT, LEVEL_01_TOTAL_POINTS
Git branch:  feature/level-01
```

## 8.2 Регистрация уровня в NavGraph

Каждый студент добавляет **одну строку** в `NavGraph.kt`:

```kotlin
// NavGraph.kt (общий файл в core/navigation/)
// STUDENTS: Add your level composable here ↓
composable(Screen.Level1.route) { Level01Screen(navController) }
composable(Screen.Level2.route) { Level02Screen(navController) }
// ... и так для каждого уровня
```

Не менять ничего другого в NavGraph!

## 8.3 Регистрация в LevelSelect

В `LevelSelectViewModel.kt` есть список уровней — студент добавляет **одну строку**:

```kotlin
val ALL_LEVELS = listOf(
    LevelInfo(1, "Boot Sector", Screen.Level1),
    LevelInfo(2, "Data Harvest", Screen.Level2),
    // ... добавить свой уровень
)
```

## 8.4 Git Workflow

```
main          ← финальный рабочий код
├── develop   ← ветка интеграции
└── feature/level-01   ← ветка студента 1
    feature/level-02   ← ветка студента 2
    ...

Правила:
1. Никогда не коммитить напрямую в main или develop
2. Создать PR из feature/level-XX → develop
3. PR проверяет тимлид перед merge
4. После merge в develop — тестируется интеграция
5. Раз в неделю develop → main (стабильная сборка)
```

## 8.5 Как избежать конфликтов

| Правило | Описание |
|---------|----------|
| Изолированные пакеты | Каждый работает ТОЛЬКО в своём `features/level_XX/` |
| Общий код — только PR | Если нужно изменить core — создать отдельный PR |
| Не изменять чужие файлы | Никогда не трогать `features/level_YY/` другого |
| NavGraph — append only | Добавлять только свою строку, не менять чужие |
| Constants.kt | Добавлять только свои константы в конец файла |

## 8.6 Checklist перед PR

```
□ Код компилируется без ошибок
□ Уровень запускается и проходится
□ Win/Lose условия работают корректно
□ Прогресс сохраняется в Room (ProgressRepository вызывается)
□ Следующий уровень разблокируется
□ Нет hardcoded строк (все в strings.xml)
□ Нет утечек памяти (ViewModel правильно очищает coroutines)
□ Код отформатирован (ktlint)
```

---

# 9. UI/UX Style Guide

## 9.1 Цветовая палитра

```kotlin
// core/ui/theme/Colors.kt
object CyberColors {
    // Фоны
    val Background       = Color(0xFF050510)
    val SurfaceDark      = Color(0xFF0A0A1F)
    val SurfaceMid       = Color(0xFF111130)

    // Основные неоновые
    val NeonCyan         = Color(0xFF00FFFF)
    val NeonGreen        = Color(0xFF00FF88)
    val NeonPink         = Color(0xFFFF00AA)
    val NeonPurple       = Color(0xFFAA00FF)
    val NeonOrange       = Color(0xFFFF6600)
    val NeonYellow       = Color(0xFFFFD700)

    // UI элементы
    val PlayerColor      = NeonGreen
    val EnemyPatrol      = NeonOrange
    val EnemyChaser      = Color(0xFFFF3030)
    val EnemyFast        = NeonPink
    val WallColor        = Color(0xFF1A3A5C)
    val EnergyPoint      = Color(0xFF00BFFF)
    val ExitColor        = NeonGreen
    val DoorColor        = NeonOrange
    val KeyColor         = NeonYellow
    val TeleportA        = NeonPurple
    val TeleportB        = NeonPink
    val TrapColor        = Color(0xFFFF0000)
    val ShieldColor      = NeonCyan

    // Текст
    val TextPrimary      = Color(0xFFE0E8FF)
    val TextSecondary    = Color(0xFF8899CC)
    val TextNeon         = NeonCyan
}
```

## 9.2 Типографика

```kotlin
// core/ui/theme/Typography.kt
val CyberTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.orbitron_bold)),
        fontSize = 32.sp,
        color = CyberColors.NeonCyan
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.orbitron_regular)),
        fontSize = 20.sp,
        color = CyberColors.TextPrimary
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.share_tech_mono)),
        fontSize = 14.sp,
        color = CyberColors.TextSecondary
    )
)
// Шрифт: Orbitron (заголовки), Share Tech Mono (текст)
// Оба доступны на Google Fonts
```

## 9.2а Landscape Layout

```
┌─────────────────────────────────────────────────────────────┐
│  ❤❤❤   SCORE: 1200   08/20 pts   ⏱ 01:23   ⏸ PAUSE       │  ← HUD (top bar)
├─────────────────────────────────────────────────────────────┤
│                                                             │
│                                                             │
│                    ИГРОВАЯ КАРТА (Canvas)                   │
│                                                             │
│  ┌───┐                                                      │
│  │ ↑ │                                                      │
│  ├───┼───┐                                                  │
│  │ ← │ → │   ← D-pad поверх Canvas (полупрозрачный)        │
│  ├───┼───┘                                                  │
│  │ ↓ │                                                      │
│  └───┘                                                      │
└─────────────────────────────────────────────────────────────┘
```

**Правила layout:**
- `screenOrientation="landscape"` в AndroidManifest — жёстко зафиксировано
- Игровой Canvas занимает **весь экран** (fillMaxSize)
- HUD — тонкая полоса сверху (`height = 48.dp`), поверх Canvas через `Box`
- D-pad — `Box` overlay в нижнем левом углу, `alpha = 0.6f` (полупрозрачный)
- Никаких отдельных панелей, sidebar'ов или мини-карт
- Карты уровней — **широкие и невысокие**: соотношение ширина:высота примерно 2:1 (например 26×13, 24×12, 28×14)

```kotlin
// Основной layout GameScreen
@Composable
fun GameScreen(...) {
    Box(modifier = Modifier.fillMaxSize()) {

        // 1. Canvas — на весь экран
        GameCanvas(
            gameState = gameState,
            modifier = Modifier.fillMaxSize()
        )

        // 2. HUD — сверху поверх Canvas
        GameHUD(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            ...
        )

        // 3. D-pad — снизу слева поверх Canvas
        DPadControl(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
                .alpha(0.6f),
            onDirection = { viewModel.onDirectionInput(it) }
        )
    }
}
```

```kotlin
// DPadControl — крестовина поверх Canvas
@Composable
fun DPadControl(
    modifier: Modifier = Modifier,
    onDirection: (Direction) -> Unit
) {
    val buttonSize = 52.dp
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NeonDPadButton("↑", buttonSize) { onDirection(Direction.UP) }
        Row {
            NeonDPadButton("←", buttonSize) { onDirection(Direction.LEFT) }
            Spacer(Modifier.size(buttonSize))
            NeonDPadButton("→", buttonSize) { onDirection(Direction.RIGHT) }
        }
        NeonDPadButton("↓", buttonSize) { onDirection(Direction.DOWN) }
    }
}

@Composable
private fun NeonDPadButton(label: String, size: Dp, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(size)
            .border(1.dp, CyberColors.NeonCyan.copy(alpha = 0.8f), RoundedCornerShape(8.dp))
            .background(Color(0x33001133), RoundedCornerShape(8.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(label, color = CyberColors.NeonCyan, fontSize = 20.sp)
    }
}
```

## 9.3 Shared UI компоненты

### GameHUD
```kotlin
@Composable
fun GameHUD(
    score: Int,
    lives: Int,
    collectedPoints: Int,
    totalPoints: Int,
    timeRemaining: Int? = null,
    keysCount: Int = 0,
    onPause: () -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        // Жизни
        repeat(lives) { HeartIcon() }
        Spacer(Modifier.weight(1f))
        // Очки
        NeonText("SCORE: $score")
        Spacer(Modifier.weight(1f))
        // Прогресс
        NeonText("$collectedPoints / $totalPoints")
        // Таймер (если есть)
        timeRemaining?.let { TimerDisplay(it) }
        // Ключи (если есть)
        if (keysCount > 0) KeyDisplay(keysCount)
        // Пауза
        IconButton(onClick = onPause) { Icon(Icons.Default.Pause, tint = NeonCyan) }
    }
}
```

### NeonButton
```kotlin
@Composable
fun NeonButton(
    text: String,
    color: Color = CyberColors.NeonCyan,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        border = BorderStroke(2.dp, color),
        modifier = Modifier.shadow(elevation = 0.dp)
    ) {
        Text(
            text = text.uppercase(),
            color = color,
            fontFamily = orbitronFamily,
            style = TextStyle(
                shadow = Shadow(color = color, blurRadius = 12f)
            )
        )
    }
}
```

### LevelCompleteScreen (shared)
```kotlin
@Composable
fun LevelCompleteScreen(
    levelNumber: Int,
    score: Int,
    starsEarned: Int,
    bestTime: Float,
    onNextLevel: () -> Unit,
    onRetry: () -> Unit,
    onMainMenu: () -> Unit
) {
    // Анимированный overlay с неоновой рамкой
    // Три звезды с анимацией появления
    // Счёт с glowing эффектом
    // Кнопки: NEXT / RETRY / MENU
}
```

## 9.4 Стиль Game Over и Pause

```kotlin
// Полупрозрачный тёмный overlay (0xAA000000)
// Неоновая рамка вокруг диалога
// Glitch эффект на заголовке "SYSTEM FAILURE" (Game Over)
// Кнопки с NeonButton компонентом
```

---

# 10. Roadmap

## Фазы разработки

### Фаза 0: Подготовка (1 неделя)
**Задача тимлида / преподавателя:**
```
□ Создать репозиторий
□ Настроить базовую структуру проекта
□ Настроить Hilt, Room, Navigation
□ Создать core/game/model/ — все data classes
□ Создать core/ui/theme/ — цвета, шрифты
□ Создать заглушки для всех экранов
□ Создать BaseLevelViewModel
□ Создать пустые пакеты для каждого уровня
□ Инструктаж команды
```

### Фаза 1: Core Infrastructure (2 неделя)
**Задача тимлида:**
```
□ GameLoop.kt
□ GameCanvas (базовый рендерер)
□ MovementSystem.kt
□ CollisionSystem.kt
□ LevelLoader.kt
□ ProgressRepository.kt
□ NavGraph.kt (заглушки для уровней)
□ GameHUD.kt
□ PauseMenu.kt
□ LevelCompleteScreen.kt
□ GameOverScreen.kt
```

### Фаза 2: Разработка уровней (3-5 неделя)
**Каждый студент параллельно:**
```
Неделя 3:
  □ Карта уровня (LevelXXMap.kt)
  □ Конфиг (LevelXXConfig.kt)
  □ Враги (LevelXXEnemies.kt)
  □ Базовый ViewModel

Неделя 4:
  □ Специальные механики уровня
  □ LevelXXScreen.kt
  □ Интеграция с shared системами
  □ Базовое тестирование

Неделя 5:
  □ Доработка по фидбеку
  □ Полировка анимаций
  □ Тестирование на устройстве
  □ PR в develop
```

### Фаза 3: Интеграция (6 неделя)
```
□ Merge всех feature branches в develop
□ Исправление merge conflicts
□ Сквозное тестирование всех уровней
□ Проверка NavGraph (переходы между уровнями)
□ Проверка сохранения прогресса
□ Проверка LevelSelect
□ Исправление багов интеграции
```

### Фаза 4: Полировка (7 неделя)
```
□ Звуковые эффекты (опционально)
□ Финальный UI polish
□ Performance optimization (Canvas)
□ Финальное QA тестирование
□ Подготовка к демо
```

### Фаза 5: Презентация (8 неделя)
```
□ Финальная сборка APK
□ Демо на реальных устройствах
□ Презентация архитектуры
□ Code review
```

## Таблица зависимостей

```
Фаза 0 (инфраструктура)
    ↓
Фаза 1 (game systems) ← БЛОКИРУЕТ всё
    ↓
Фазы 2 (уровни) ← параллельно, независимо
    ↓
Фаза 3 (интеграция)
    ↓
Фаза 4 (полировка)
    ↓
Фаза 5 (презентация)
```

> **Критически важно:** Фаза 1 (core infrastructure) должна быть готова до начала разработки уровней. Иначе студенты будут блокированы.

---

## Приложение: AndroidManifest (ориентация)

```xml
<!-- AndroidManifest.xml -->
<activity
    android:name=".MainActivity"
    android:screenOrientation="landscape"
    android:configChanges="orientation|screenSize|keyboardHidden">
</activity>
```

## Приложение: build.gradle (зависимости)

```kotlin
// app/build.gradle.kts
dependencies {
    // Jetpack Compose
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.activity:activity-compose:1.8.2")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.6")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.50")
    kapt("com.google.dagger:hilt-android-compiler:2.50")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

    // Google Fonts (Orbitron, Share Tech Mono)
    implementation("androidx.compose.ui:ui-text-google-fonts:1.6.1")
}
```

---

*Cyber Maze TZ v1.0 | Для внутреннего использования команды разработки*  
*Все вопросы — через Issues в репозитории проекта*
