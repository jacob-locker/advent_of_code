import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

abstract class BaseDayObjectTest(private val day: BaseDay,
                                private val partOneSimpleExpected: Any? = null,
                                private val partOneExpected: Any? = null,
                                private val partTwoSimpleExpected: Any? = null,
                                private val partTwoExpected: Any? = null) {

    protected val simpleInput = InputRetriever().retrieveInput(day.number, test = true)
    protected val fullInput = InputRetriever().retrieveInput(day.number)

    @Test
    fun `part one simple`() {
        runBlocking {
            partOneSimpleExpected?.let { assertEquals(it, day.partOne(simpleInput)) }
        }
    }

    @Test
    fun `part one`() {
        runBlocking {
            partOneExpected?.let { assertEquals(it, day.partOne(fullInput)) }
        }
    }

    @Test
    fun `part two simple`() {
        runBlocking {
            partTwoSimpleExpected?.let { assertEquals(it, day.partTwo(simpleInput)) }
        }
    }

    @Test
    fun `part two`() {
        runBlocking {
            partTwoExpected?.let { assertEquals(it, day.partTwo(fullInput)) }
        }
    }

}