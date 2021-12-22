package day21

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DiracGameTest {
    @Test
    fun `check next roll changes state`() {
        var game = DiracGame(10, Pair(Player(8, 0), Player(7, 0)))
        assertTrue(game.isPlayerOneTurn)
        assertEquals(8, game.players.first.position)
        assertEquals(7, game.players.second.position)

        game = game.nextRoll(1)
        assertTrue(game.isPlayerOneTurn)
        assertEquals(8, game.players.first.position)
        assertEquals(7, game.players.second.position)

        game = game.nextRoll(2)
        assertTrue(game.isPlayerOneTurn)
        assertEquals(8, game.players.first.position)
        assertEquals(7, game.players.second.position)

        game = game.nextRoll(3)
        assertFalse(game.isPlayerOneTurn)
        assertEquals(4, game.players.first.position)
        assertEquals(7, game.players.second.position)

        game = game.nextRoll(1)
        assertFalse(game.isPlayerOneTurn)
        assertEquals(4, game.players.first.position)
        assertEquals(7, game.players.second.position)

        game = game.nextRoll(2)
        assertFalse(game.isPlayerOneTurn)
        assertEquals(4, game.players.first.position)
        assertEquals(7, game.players.second.position)

        game = game.nextRoll(3)
        assertTrue(game.isPlayerOneTurn)
        assertEquals(4, game.players.first.position)
        assertEquals(3, game.players.second.position)
    }
}