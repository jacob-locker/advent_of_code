package day5

class OceanFloor(private val size: Int) {
    val grid = Array(size) { Array(size) { 0 } }

    fun addVents(vents: Vents, ignoresDiagonals: Boolean = true) {
        if (ignoresDiagonals && vents.isDiagonal) {
            return
        }
        val (y1, y2) = if (vents.y1 <= vents.y2) vents.y1 to vents.y2 else vents.y2 to vents.y1
        val (x1, x2) = if (vents.x1 <= vents.x2) vents.x1 to vents.x2 else vents.x2 to vents.x1

        for (row in y1..y2) {
            for (col in x1..x2) {
                if (vents.isDiagonal) {
                    if (vents.isRightDiagonal && row - y1 == col - x1) {
                        grid[row][col] += 1
                    } else if (vents.isLeftDiagonal && row - y1 == (x2 - x1) - (col - x1)) {
                        grid[row][col] += 1
                    }
                } else {
                    grid[row][col] += 1
                }
            }
        }
    }

    fun getOverlaps() = grid.sumOf { row -> row.count { it >= 2 } }

    fun display() {
        for (i in grid.indices) {
            println(grid[i].contentToString())
        }
    }
}

data class Vents(val x1: Int, val y1: Int, val x2: Int, val y2: Int) {
    val isDiagonal = x1 != x2 && y1 != y2
    val isLeftDiagonal = x1 < x2 && y1 > y2 || x1 > x2 && y2 > y1
    val isRightDiagonal = x1 < x2 && y2 > y1 || x1 > x2 && y1 > y2
}

fun String.parseVents() = split(" -> ").let { splitInput ->
    val firstPoint = splitInput[0].split(",")
    val secondPoint = splitInput[1].split(",")
    Vents(firstPoint[0].toInt(), firstPoint[1].toInt(), secondPoint[0].toInt(), secondPoint[1].toInt())
}

fun List<String>.parseVentList() = map { it.parseVents() }