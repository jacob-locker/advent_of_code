package day11

import DayVisualization
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import copy
import getInputLines
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class Day11Visualization : DayVisualization(11) {

    private val gridFlow = MutableStateFlow(OctopusGrid(Array(10) { IntArray(10) }))
    override val partOneOutputFlow: Flow<String>
        get() = _partOneOutputFlow
    private val _partOneOutputFlow = MutableStateFlow("")

    override val partTwoOutputFlow: Flow<String>
        get() = _partTwoOutputFlow
    private val _partTwoOutputFlow = MutableStateFlow("")

    private var gridJob: Job? = null

    override fun startPartOne(input: String, coroutineScope: CoroutineScope) {
        gridJob?.cancel()
        gridJob = coroutineScope.launch {
            var octopusGrid = input.getInputLines().parseOctopusGrid()
            repeat(100) {
                octopusGrid = OctopusGrid(octopusGrid.values.copy(), octopusGrid.numberOfFlashes)
                octopusGrid.step()
                gridFlow.value = octopusGrid
                _partOneOutputFlow.value = "Number of Flashes: ${octopusGrid.numberOfFlashes}"
                delay(300)
            }
        }

    }

    override fun startPartTwo(input: String, coroutineScope: CoroutineScope) {
        gridJob?.cancel()
        gridJob = coroutineScope.launch {
            var octopusGrid = input.getInputLines().parseOctopusGrid()
            var count = 1
            while(!octopusGrid.allOctopusesAreFlashing()) {
                octopusGrid = OctopusGrid(octopusGrid.values.copy(), octopusGrid.numberOfFlashes)
                octopusGrid.step()
                gridFlow.value = octopusGrid
                _partTwoOutputFlow.value = "Number of Steps Until All Flashing: ${count++}"
                delay(100)
            }
        }
    }

    @Composable
    override fun VisualizePartOne(modifier: Modifier) {
        val octopusGrid = gridFlow.collectAsState(OctopusGrid(Array(10) { IntArray(10) }))
        OctopusGridLayout(octopusGrid.value, modifier)

    }

    @Composable
    override fun VisualizePartTwo(modifier: Modifier) {
        val octopusGrid = gridFlow.collectAsState(OctopusGrid(Array(10) { IntArray(10) }))
        OctopusGridLayout(octopusGrid.value, modifier)
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun OctopusGridLayout(octopusGrid: OctopusGrid, modifier: Modifier) {
        Surface(modifier = modifier, color = Color.Black) {
            Column(verticalArrangement = Arrangement.SpaceEvenly) {
                repeat(octopusGrid.size) { row ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        repeat(octopusGrid.size) { col ->
                            OctopusLayout(octopusGrid.values[row][col])
                        }
                    }
                }
            }

        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun OctopusLayout(value: Int) {
            Surface(
                shape = CircleShape,
                modifier = Modifier.graphicsLayer {
                    //this.alpha = if (value == 0) 1F else value / 36F
                    this.scaleX = if (value == 0) 1F else value / 9F
                    this.scaleY = if (value == 0) 1F else value / 9F
                }.drawWithContent {
                    val colors = listOf(Color.Blue, Color.Transparent)
                    drawContent()
                    drawCircle(
                        brush = Brush.radialGradient(colors),
                        blendMode = BlendMode.DstIn
                    )
                }.size(80.dp),
                color = Color.Blue
            ) {

            }
    }
}