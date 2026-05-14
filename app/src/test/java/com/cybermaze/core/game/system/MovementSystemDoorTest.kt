package com.cybermaze.core.game.system

import com.cybermaze.core.game.level.LevelLoader
import com.cybermaze.core.game.model.Direction
import com.cybermaze.core.game.model.GameMap
import com.cybermaze.core.game.model.LevelConfig
import com.cybermaze.core.game.model.Player
import com.cybermaze.core.game.model.Position
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MovementSystemDoorTest {

    private val cfg = LevelConfig(
        levelNumber = 1,
        title = "Door test",
        description = "",
        requiredPoints = 0,
        totalPoints = 0
    )

    private val movement = MovementSystem()

    /** Row: `#X..DE#` — player at (1,1), empties, door (4,1), exit (5,1). */
    private fun mapWithDoor(): GameMap =
        LevelLoader.loadFromStringArray(
            arrayOf(
                "#######",
                "#X..DE#",
                "#######"
            ),
            cfg
        )

    @Test
    fun `canPlayerMove blocks door without key`() {
        val map = mapWithDoor()
        val player = Player(position = Position(3, 1), keysCollected = 0)
        assertFalse(movement.canPlayerMove(player, Direction.RIGHT, map))
    }

    @Test
    fun `canPlayerMove allows door with key`() {
        val map = mapWithDoor()
        val player = Player(position = Position(3, 1), keysCollected = 1)
        assertTrue(movement.canPlayerMove(player, Direction.RIGHT, map))
    }

    @Test
    fun `movePlayer steps onto door when key held`() {
        val map = mapWithDoor()
        val player = Player(position = Position(3, 1), keysCollected = 1)
        val moved = movement.movePlayer(player, Direction.RIGHT, map)
        assertEquals(Position(4, 1), moved.position)
        assertEquals(Direction.RIGHT, moved.direction)
    }

    @Test
    fun `canMove blocks door for generic pathing`() {
        val map = mapWithDoor()
        assertFalse(movement.canMove(Position(3, 1), Direction.RIGHT, map))
    }

    @Test
    fun `canMove blocks stepping onto door for enemies and generic checks`() {
        val map = LevelLoader.loadFromStringArray(
            arrayOf(
                "#####",
                "#X..#",
                "#.D.#",
                "#####"
            ),
            cfg
        )
        // Standing above the door: step DOWN onto DOOR must stay blocked for [canMove].
        assertFalse(movement.canMove(Position(2, 1), Direction.DOWN, map))
    }
}
