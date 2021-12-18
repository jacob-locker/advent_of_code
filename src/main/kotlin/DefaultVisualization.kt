import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DefaultVisualization(private val day: BaseDay) : DayVisualization(day.number) {
    override fun launchPartOneJob(input: String, coroutineScope: CoroutineScope) = coroutineScope.launch(Dispatchers.Default) {
        _partOneOutputFlow.value = day.partOne(input).toString()
    }

    override fun launchPartTwoJob(input: String, coroutineScope: CoroutineScope) = coroutineScope.launch(Dispatchers.Default) {
        _partTwoOutputFlow.value = day.partTwo(input).toString()
    }

    @Composable
    override fun VisualizePartOne(modifier: Modifier) {

    }

    @Composable
    override fun VisualizePartTwo(modifier: Modifier) {

    }
}