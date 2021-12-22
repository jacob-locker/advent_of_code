package day21

import BaseDay
import getInputLines

class Day21 : BaseDay(21, "Dirac Dice") {
    override suspend fun partOne(input: String) = solveDay21(input) { players ->
        val dice = DeterministicDice()
        val game = Game(10, players, dice)
        var endResult: Pair<Player, Player>? = null
        while (endResult == null) {
            endResult = game.playTurn()
        }

        (endResult.second.score * dice.numberOfRolls).toLong()
    }

    override suspend fun partTwo(input: String) = solveDay21(input) { players ->
        val game = DiracGame(10, players)
        val wins = playDiracGame(game)

        if (wins.first > wins.second) {
            wins.first
        } else {
            wins.second
        }
    }

    private fun solveDay21(input: String, solver: (Pair<Player, Player>) -> Long) =
        input.getInputLines().parsePlayers().let {
            Pair(it[0], it[1])
        }.let(solver)
}

fun List<String>.parsePlayers() = map { Player(it.split(": ").last().toInt()) }

class Player(var position: Int, var score: Int = 0) {
    fun copy() = Player(position = position, score = score)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Player) return false

        if (position != other.position) return false
        if (score != other.score) return false

        return true
    }

    override fun hashCode(): Int {
        var result = position
        result = 31 * result + score
        return result
    }
}

class Game(private val boardSize: Int, private val players: Pair<Player, Player>, private val dice: Dice) {
    companion object {
        const val WINNING_SCORE = 1000
    }

    /**
     * Returns Pair of Winning Player to Losing Player if the game ended
     */
    fun playTurn(): Pair<Player, Player>? =
        when {
            playTurn(
                10,
                players.first,
                dice.roll() + dice.roll() + dice.roll()
            ).score >= WINNING_SCORE -> Pair(players.first, players.second)
            playTurn(
                10,
                players.second,
                dice.roll() + dice.roll() + dice.roll()
            ).score >= WINNING_SCORE -> Pair(players.second, players.first)
            else -> null
        }
}

fun playTurn(boardSize: Int, player: Player, roll: Int): Player =
    player.apply {
        position = (position + roll) % (boardSize)
        if (position == 0) {
            position = boardSize
        }
        score += position
    }

fun playDiracGame(diracGame: DiracGame,
                  memo: MutableMap<DiracGame, Pair<Long, Long>> = mutableMapOf()): Pair<Long, Long> {
    if (diracGame in memo) {
        return memo[diracGame]!!
    }
    if (diracGame.isOver) {
        return if (diracGame.whoWon == 1) Pair(1, 0) else Pair(0, 1)
    }

    memo[diracGame] = playDiracGame(diracGame.nextRoll(1), memo) +
            playDiracGame(diracGame.nextRoll(2), memo) +
            playDiracGame(diracGame.nextRoll(3), memo)
    return memo[diracGame]!!
}

operator fun Pair<Long, Long>.times(other: Int) = Pair(first * other, second * other)
operator fun Pair<Long, Long>.plus(other: Pair<Long, Long>) = Pair(first + other.first, second + other.second)

class DiracGame(
    private val boardSize: Int, val players: Pair<Player, Player>,
    private val rollIndex: Int = 0,
    private val rolls: IntArray = IntArray(ROLLS_PER_TURN) { 0 },
    val isPlayerOneTurn: Boolean = true,
    private val playerOneWon: Boolean = false,
    private val playerTwoWon: Boolean = false,
) {
    companion object {
        const val WINNING_SCORE = 21
        const val ROLLS_PER_TURN = 3
    }

    val isOver = playerOneWon || playerTwoWon
    val whoWon = if (playerOneWon) 1 else if (playerTwoWon) 2 else 0

    fun nextRoll(roll: Int): DiracGame {
        val turnChange = rollIndex == ROLLS_PER_TURN - 1
        val newPlayers = if (turnChange) {
            val total = rolls.slice(0 until rolls.lastIndex).sum() + roll
            if (isPlayerOneTurn) Pair(playTurn(boardSize, players.first.copy(), total), players.second.copy()) else Pair(players.first.copy(), playTurn(boardSize, players.second.copy(), total))
        } else {
            players
        }

        return DiracGame(
            boardSize, newPlayers,
            rollIndex = (rollIndex + 1) % ROLLS_PER_TURN,
            rolls = if (turnChange) IntArray(ROLLS_PER_TURN) { 0 } else IntArray(ROLLS_PER_TURN) { if (it == rollIndex) roll else rolls[it] },
            isPlayerOneTurn = if (turnChange) !isPlayerOneTurn else isPlayerOneTurn,
            playerOneWon = if (isPlayerOneTurn) newPlayers.first.score >= WINNING_SCORE else playerOneWon,
            playerTwoWon = if (!isPlayerOneTurn) newPlayers.second.score >= WINNING_SCORE else playerTwoWon
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DiracGame) return false

        if (players != other.players) return false
        if (playerOneWon != other.playerOneWon) return false
        if (playerTwoWon != other.playerTwoWon) return false
        if (isPlayerOneTurn != other.isPlayerOneTurn) return false
        if (rolls.sum() != other.rolls.sum()) return false
        if (rollIndex != other.rollIndex) return false
        return true
    }

    override fun hashCode(): Int {
        var result = players.hashCode()
        result = 31 * result + playerOneWon.hashCode()
        result = 31 * result + playerTwoWon.hashCode()
        result = 31 * result + isPlayerOneTurn.hashCode()
        result = 31 * result + rolls.sum().hashCode()
        result = 31 * result + rollIndex.hashCode()
        return result
    }
}

interface Dice {
    val numberOfRolls: Int
    fun roll(): Int
}

class DeterministicDice : Dice {
    var rollValue = 1
    private var _numberOfRolls = 0

    override val numberOfRolls: Int
        get() = _numberOfRolls

    override fun roll(): Int {
        _numberOfRolls++
        return rollValue++
    }
}
