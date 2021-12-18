import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import java.awt.Dimension

fun main() = application {
    Window(title = "Advent of Code 2021", resizable = false, onCloseRequest = ::exitApplication) {
        this.window.size = Dimension(928, 572)
        App(retrieveImplementedDays(), retrieveVisualizations())
    }
}

var executeDayJob: Job? = null

@Composable
@Preview
fun App(dayObjs: Map<Int, BaseDay>, visualizations: Map<Int, DayVisualization>) {
    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val days = arrayOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa")
                for (day in days) {
                    Text(day, style = MaterialTheme.typography.h6, modifier = Modifier.width(128.dp))
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                for (day in 28..30) {
                    DayLayout(dayObjs[day], visualizations[day], day, isEmpty = true)
                }
                for (day in 1..4) {
                    DayLayout(dayObjs[day], visualizations[day], day, isEmpty = false)
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                for (day in 5..11) {
                    DayLayout(dayObjs[day], visualizations[day], day, isEmpty = false)
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                for (day in 12..18) {
                    DayLayout(dayObjs[day], visualizations[day], day, isEmpty = false)
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                for (day in 19..25) {
                    DayLayout(dayObjs[day], visualizations[day], day, isEmpty = false)
                }
            }
        }
    }
}

@Composable
fun VisualizationLayout(title: String, viz: DayVisualization, showVisualization: MutableState<Boolean>) {
    val vizPartOne = remember { mutableStateOf<Boolean?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val size = 800.dp

    Column(modifier = Modifier.border(width = 1.dp, MaterialTheme.colors.primary).padding(8.dp)) {
        Row(modifier = Modifier.width(width = size), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Day ${viz.number}: $title", style = MaterialTheme.typography.h4)
            IconButton(onClick = { showVisualization.value = false }) {
                Icon(imageVector = Icons.Filled.Close, contentDescription = "Close")
            }
        }

        if (vizPartOne.value == false) {
            VizOutputLayout(viz.partTwoOutputFlow)
            viz.VisualizePartTwo(modifier = Modifier.size(width = size, height = size))
        } else {
            VizOutputLayout(viz.partOneOutputFlow)
            viz.VisualizePartOne(modifier = Modifier.size(width = size, height = size))
        }

        Row(modifier = Modifier.width(width = size)) {
            Button(modifier = Modifier.fillMaxWidth(0.5f).padding(start = 8.dp, end = 4.dp), onClick = {
                viz.startPartOne(DayRetriever().retrieveInput(viz.number), coroutineScope).also {
                    vizPartOne.value = true
                }
            }) { Text("Part One") }
            Button(modifier = Modifier.fillMaxWidth().padding(start = 4.dp, end = 8.dp), onClick = {
                viz.startPartTwo(DayRetriever().retrieveInput(viz.number), coroutineScope).also {
                    vizPartOne.value = false
                }
            }) { Text("Part Two") }
        }

    }
}

@Composable
fun VizOutputLayout(outputFlow: Flow<String>) {
    val output = outputFlow.collectAsState("")
    Text(style = MaterialTheme.typography.h5, text = output.value)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DayLayout(day: BaseDay?, viz: DayVisualization?, dayNumber: Int, isEmpty: Boolean = false) {
    val modifier = Modifier.size(128.dp).padding(top = 2.dp, bottom = 2.dp)
    val showVisualization = remember { mutableStateOf(false) }
    if (showVisualization.value) {
        UndecoratedWindowAlertDialogProvider.AlertDialog(onDismissRequest = { showVisualization.value = false }) {
            VisualizationLayout(day!!.title, viz ?: DefaultVisualization(day), showVisualization)
        }
    }
    if (day != null) {
        Button(onClick = {
            showVisualization.value = true
            //if (viz == null) executeDay(coroutineScope, day)
        }, modifier = modifier) {
            Text("${day.number}")
        }
    } else if (isEmpty) {
        Button(onClick = {}, enabled = false, modifier = modifier) {
            Text("$dayNumber")
        }
    } else {
        OutlinedButton(onClick = {}, modifier = modifier) {
            Text("$dayNumber")
        }
    }
}

fun executeDay(coroutineScope: CoroutineScope, day: BaseDay) {
    executeDayJob?.cancel()
    executeDayJob = coroutineScope.launch(Dispatchers.Default) {
        try {
            printDay(day.number, day.title) {
                printPart { day.partOne(it) }
                printPart { day.partTwo(it) }
            }
        } catch (e: Exception) {
        }
    }
}

class DayScope {
    val parts: List<suspend ((String) -> Any)>
        get() = _parts

    private val _parts = mutableListOf<suspend ((String) -> Any)>()

    fun printPart(partMethod: suspend ((String) -> Any)) {
        _parts.add(partMethod)
    }
}

suspend fun printDay(dayNumber: Int, dayTitle: String, dayScopeAction: DayScope.() -> Unit) = coroutineScope {
    println("-- Day $dayNumber: $dayTitle --")
    val dayInput = DayRetriever().retrieveInput(dayNumber)

    //println("Input: ${dayInput.getInputLines()}")

    val dayScope = DayScope()
    dayScopeAction.invoke(dayScope)

    dayScope.parts.forEachIndexed { index, partMethod ->
        println("Part ${index + 1} Output: ${partMethod(dayInput)}")
    }
    println()
}