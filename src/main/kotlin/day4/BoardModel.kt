package day4

class Board(private val id: Int, private val board: List<List<Int>>) {
    private val remainingBoardNumbers = HashSet<Int>().apply { board.forEach { row -> row.forEach { add(it) } } }
    private val rows = mutableListOf<MutableSet<Int>>().apply { board.forEach { row -> add(row.toHashSet())} }.toList()
    private val cols = mutableListOf<MutableSet<Int>>().apply {
        board.forEach { row ->
            row.forEachIndexed { colIndex, value ->
                val col = if (colIndex == size) HashSet() else get(colIndex)
                col.add(value)
                if (colIndex >= size) {
                    add(col)
                }
            }
        }
    }

    private val boardComponents = mutableListOf<MutableSet<Int>>().apply {
        addAll(rows)
        addAll(cols)
    }

    fun markNumber(number: Int) : BoardResult {
        var result: BoardResult = MissedResult
        var winningBoard = false
        boardComponents.forEach {
            if (it.remove(number)) {
                result = MarkedResult
            }

            if (it.isEmpty()) {
                winningBoard = true
            }
        }

        remainingBoardNumbers.remove(number)

        if (winningBoard) {
            result = WinResult(remainingBoardNumbers.sum() * number)
        }

        return result
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return if (other is Board) id == other.id else false
    }
}

sealed class BoardResult

class WinResult(val score: Int) : BoardResult()
object MarkedResult : BoardResult()
object MissedResult : BoardResult()

fun String.parseNumberStream() = split(",").map { it.toInt() }

fun List<String>.parseBoards() = mapIndexed { index, str -> str.parseBoard(index) }

fun String.parseBoard(id: Int) : Board {
    val rows = split(System.lineSeparator()).map { it.trim().split("\\s+".toRegex()).map { it.toInt() } }
    return Board(id, rows)
}