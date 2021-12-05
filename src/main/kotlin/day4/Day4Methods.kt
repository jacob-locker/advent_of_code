package day4

fun solveDay4(input: String, solver: (List<Int>, List<Board>) -> Int) : Int {
    val splitInput = input.split("\r\n\r\n")
    val pair = Pair(splitInput[0].replace(System.lineSeparator(), "").parseNumberStream(),
        splitInput.slice(1 until splitInput.size).parseBoards())
    return solver(pair.first, pair.second)
}

fun findWinningScore(input: String) = solveDay4(input) { bingoNumbers, boards ->
    bingoNumbers.forEach { number ->
        boards.forEach { board ->
            val result = board.markNumber(number)
            if (result is WinResult) {
                return@solveDay4 result.score
            }
        }
    }

    -1
}

fun findLastWinningScore(input: String) = solveDay4(input) { bingoNumbers, boards ->
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
    lastWinningScore
}