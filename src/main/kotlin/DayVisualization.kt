import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class DayVisualization(val number: Int) {
    val partOneOutputFlow
    get() = _partOneOutputFlow.asStateFlow()

    protected val _partOneOutputFlow = MutableStateFlow("")

    val partTwoOutputFlow
    get() = _partTwoOutputFlow.asStateFlow()

    protected val _partTwoOutputFlow = MutableStateFlow("")
    private var job: Job? = null

    fun startPartOne(input: String, coroutineScope: CoroutineScope) {
        job?.cancel()
        job = launchPartOneJob(input, coroutineScope)
    }

    protected abstract fun launchPartOneJob(input: String, coroutineScope: CoroutineScope): Job

    fun startPartTwo(input: String, coroutineScope: CoroutineScope) {
        job?.cancel()
        job = launchPartTwoJob(input, coroutineScope)
    }

    protected abstract fun launchPartTwoJob(input: String, coroutineScope: CoroutineScope): Job

    @Composable
    abstract fun VisualizePartOne(modifier: Modifier)

    @Composable
    abstract fun VisualizePartTwo(modifier: Modifier)
}