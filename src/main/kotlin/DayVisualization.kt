import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

abstract class DayVisualization(val number: Int) {
    abstract val partOneOutputFlow: Flow<String>

    abstract val partTwoOutputFlow: Flow<String>

    abstract fun startPartOne(input: String, coroutineScope: CoroutineScope)

    abstract fun startPartTwo(input: String, coroutineScope: CoroutineScope)

    @Composable
    abstract fun VisualizePartOne(modifier: Modifier)

    @Composable
    abstract fun VisualizePartTwo(modifier: Modifier)
}