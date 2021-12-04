package day4

fun parseDay4Input(input: String) : Pair<List<Int>, List<Board>> {
    val splitInput = input.split("\r\n\r\n")
    return Pair(splitInput[0].replace(System.lineSeparator(), "").parseNumberStream(), splitInput.slice(1 until splitInput.size).parseBoards())
}

fun findWinningScore(input: String) = parseDay4Input(input).let { findWinningScore(it.first, it.second) }
fun findWinningScore(bingoNumbers: List<Int>, boards: List<Board>): Int {
    bingoNumbers.forEach { number ->
        boards.forEach { board ->
            val result = board.markNumber(number)
            if (result is WinResult) {
                return result.score
            }
        }
    }

    return -1
}

fun findLastWinningScore(input: String) = parseDay4Input(input).let { findLastWinningScore(it.first, it.second) }
fun findLastWinningScore(bingoNumbers: List<Int>, boards: List<Board>): Int {
    var lastWinningScore = -1
    val winningBoards = HashSet<Board>()
    bingoNumbers.forEach { number ->
        boards.forEach { board ->
            if (!winningBoards.contains(board)) {
                val result = board.markNumber(number)
                if (result is WinResult) {
                    lastWinningScore = result.score
                    winningBoards.add(board)
                }
            }
        }
    }
    return lastWinningScore
}