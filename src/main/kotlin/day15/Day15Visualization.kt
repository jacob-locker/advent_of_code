package day15

import ColorScheme
import DayVisualization
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import getInputLines
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import ui.ColorSchemeSelection

class Day15Visualization : DayVisualization(15) {
    @Stable
    class UiState(val grid: Array<IntArray>) {
        var visitedStates
        get() = _visitedStates
        set(value) {
            _visitedStates = value
        }

        var pathStates = Array(0) { Array(0) { MutableStateFlow(false) } }

        private var _visitedStates = Array(0) { Array(0) { MutableStateFlow(false) } }
    }

    private val stateFlow = MutableStateFlow(UiState(emptyArray()))

    override fun launchPartOneJob(input: String, coroutineScope: CoroutineScope) =
        coroutineScope.launch(Dispatchers.Default) {
            val riskMap = input.getInputLines().parseRiskMap()
            val uiState = UiState(riskMap.grid).apply {
                visitedStates = Array(riskMap.size) { Array(riskMap.size) { MutableStateFlow(false) } }
                pathStates = Array(riskMap.size) { Array(riskMap.size) { MutableStateFlow(false) } }
            }
            var path = riskMap.findLeastRiskPath()
            stateFlow.value = uiState
            riskMap.enteredList.forEachIndexed { _, riskState ->
                uiState.visitedStates[riskState.row][riskState.col].value = true
                _partOneOutputFlow.value = "Risk Level: ${riskState.risk}"
                delay(5)
            }

            while (path != null) {
                uiState.pathStates[path.row][path.col].value = true
                path = path.previousState
            }
        }

    override fun launchPartTwoJob(input: String, coroutineScope: CoroutineScope) =
        coroutineScope.launch(Dispatchers.Default) {
            val riskMap = input.getInputLines().parseRiskMap()
            riskMap.grow(5)
            val riskLevel = riskMap.findLeastRiskPath()?.risk?.toString() ?: ""
            _partTwoOutputFlow.value = "Risk Level: $riskLevel"
        }

    @Composable
    override fun VisualizePartOne(modifier: Modifier) {
        val state = stateFlow.collectAsState()
        PartOneLayout(state.value, modifier)
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun PartOneLayout(uiState: UiState, modifier: Modifier) {
        Column {
            val colorSchemeState = remember { mutableStateOf(ColorScheme.WHITE_BLUE_GREY_BLK) }
            ColorSchemeSelection { colorSchemeState.value = it }
            Column(modifier = modifier.padding(top = 8.dp).border(4.dp, color = colorSchemeState.value.colors[0])) {
                repeat(uiState.grid.size) { row ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        for (col in uiState.grid[row].indices) {
                            GridCellLayout(row, col, uiState, colorSchemeState.value)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun GridCellLayout(row: Int, col: Int, uiState: UiState, colorScheme: ColorScheme) {
        val visitedState = uiState.visitedStates[row][col].collectAsState()
        val pathState = uiState.pathStates[row][col].collectAsState()
        if (pathState.value) {
            Surface(
                color = Color.Red,
                elevation = 16.dp,
                modifier = Modifier.size((800 / uiState.grid.size).dp)
            ) {}
        } else if (visitedState.value) {
            VisitedLayout(colorScheme.colors[0], 800 / uiState.grid.size)
        } else {
            EmptyLayout(colorScheme, uiState.grid[row][col], 800 / uiState.grid.size)
        }
    }

    @Composable
    fun VisitedLayout(color: Color, size: Int) {
        Surface(
            color = color,
            elevation = 16.dp,
            modifier = Modifier.size(size.dp)
        ) {}
    }

    @Composable
    fun EmptyLayout(colorScheme: ColorScheme, riskLevel: Int, size: Int) {
        val color = when(riskLevel) {
            1, 2, 3 -> colorScheme.colors[1]
            4, 5 -> colorScheme.colors[2]
            6, 7 -> colorScheme.colors[3]
            8, 9 -> colorScheme.colors[4]
            else -> Color.Black
        }

        Surface(
            color = color,
            elevation = 16.dp,
            modifier = Modifier.size(size.dp)
        ) {}
    }

    @Composable
    override fun VisualizePartTwo(modifier: Modifier) {

    }
}