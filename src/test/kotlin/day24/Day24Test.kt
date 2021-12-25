package day24

import BaseDayObjectTest
import getInputLines
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.util.*

class Day24Test : BaseDayObjectTest(Day24()){

    @Test
    fun `investigate`() {
        runBlocking {
            val instructions = fullInput().getInputLines().parseInstructions()

            with(Program(LinkedList<Long>().apply { for (i in 0 until 14) { add(1L) } })) {
                println("11111111111111: " + run(instructions))
            }
        }
    }
}