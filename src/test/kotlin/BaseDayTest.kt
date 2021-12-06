import org.junit.Test
import kotlin.test.assertEquals

abstract class BaseDayTest<T>(
    private val day: Int,
    private val partOneMethod: ((String) -> T)? = null,
    private val partOneSimpleExpected: T? = null,
    private val partOneExpected: T? = null,
    private val partTwoMethod: ((String) -> T)? = null,
    private val partTwoSimpleExpected: T? = null,
    private val partTwoExpected: T? = null
) {
    protected val simpleInput = InputRetriever().retrieveInput(day, test = true)
    protected val fullInput = InputRetriever().retrieveInput(day)

    @Test
    fun `part one simple`() {
        partOneSimpleExpected?.let { assertEquals(it, partOneMethod!!(simpleInput)) }
    }

    @Test
    fun `part one`() {
        partOneExpected?.let { assertEquals(it, partOneMethod!!(fullInput)) }
    }

    @Test
    fun `part two simple`() {
        partTwoSimpleExpected?.let { assertEquals(it, partTwoMethod!!(simpleInput)) }
    }

    @Test
    fun `part two`() {
        partTwoExpected?.let { assertEquals(it, partTwoMethod!!(fullInput)) }
    }
}