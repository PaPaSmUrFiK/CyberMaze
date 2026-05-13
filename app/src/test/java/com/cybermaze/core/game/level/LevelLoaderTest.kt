package com.cybermaze.core.game.level

import com.cybermaze.core.game.model.LevelConfig
import com.cybermaze.core.game.model.Position
import com.cybermaze.core.game.model.TileType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LevelLoaderTest {

    private val sampleConfig = LevelConfig(
        levelNumber = 1,
        title = "Test",
        description = "",
        requiredPoints = 1,
        totalPoints = 2
    )

    @Test
    fun `loads valid map and finds spawn and exit`() {
        val layout = arrayOf(
            "#####",
            "#X.E#",
            "#####"
        )
        val map = LevelLoader.loadFromStringArray(layout, sampleConfig)
        assertEquals(5, map.width)
        assertEquals(3, map.height)
        assertEquals(Position(1, 1), map.playerSpawn)
        assertEquals(Position(3, 1), map.exitPosition)
        assertEquals(TileType.WALL, map.getTile(Position(0, 0))?.type)
        assertEquals(TileType.EXIT, map.getTile(Position(3, 1))?.type)
    }

    @Test
    fun `walls block movement`() {
        val map = LevelLoader.loadFromStringArray(
            arrayOf(
                "###",
                "#.#",
                "###"
            ),
            sampleConfig
        )
        assertTrue(map.isPassable(Position(1, 1)))
        assertTrue(!map.isPassable(Position(0, 0)))
    }

    @Test
    fun `validate detects missing spawn`() {
        val errors = LevelLoader.validateLayout(arrayOf("###", "#.#", "###"))
        assertTrue(errors.any { it.contains("spawn") })
    }
}
