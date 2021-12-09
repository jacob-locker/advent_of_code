import kotlinx.coroutines.runBlocking
import org.junit.Test

class DayRetrieverTest {
    @Test
    fun `retrieve input remotely`() {
        runBlocking {
            DayRetriever().retrieveInputRemotely(2)
        }
    }

    @Test
    fun `retrieve input`() {
        runBlocking {
            DayRetriever().retrieveInput(8)
        }
    }
}