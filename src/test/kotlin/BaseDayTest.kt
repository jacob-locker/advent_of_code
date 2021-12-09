import kotlinx.coroutines.runBlocking
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
    suspend fun simpleInput() = DayRetriever().retrieveInput(day, test = true)
    suspend fun fullInput() = DayRetriever().retrieveInput(day)

    @Test
    fun `part one simple`() {
        runBlocking { partOneSimpleExpected?.let { assertEquals(it, partOneMethod!!(simpleInput())) } }
    }

    @Test
    fun `part one`() {
        runBlocking { partOneExpected?.let { assertEquals(it, partOneMethod!!(fullInput())) } }
    }

    @Test
    fun `part two simple`() {
        runBlocking { partTwoSimpleExpected?.let { assertEquals(it, partTwoMethod!!(simpleInput())) } }
    }

    @Test
    fun `part two`() {
        runBlocking { partTwoExpected?.let { assertEquals(it, partTwoMethod!!(fullInput())) } }
    }
}