package ru.nstu.isma.grin.launcher

import javafx.application.Application
import javafx.application.Platform
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.scene.paint.Color
import javafx.stage.Stage
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.stopKoin
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.axis.model.Direction
import ru.nstu.grin.concatenation.canvas.controller.ConcatenationCanvasController
import ru.nstu.grin.concatenation.canvas.view.ConcatenationView
import ru.nstu.grin.concatenation.cartesian.model.CartesianSpace
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.function.model.LineType
import ru.nstu.grin.concatenation.koin.MainGrinScope
import ru.isma.next.exchange.format.readMetadata
import ru.isma.next.exchange.format.readAllPointsSequence
import java.io.File
import java.util.concurrent.Executors

class GrinApplication : Application(), KoinComponent {
    lateinit var scope: MainGrinScope
    private var config: GrinCommandLineConfig? = null
    private val executor = Executors.newVirtualThreadPerTaskExecutor()

    init {
        grinKoinStart()
    }

    override fun init() {
        super.init()
        val params = parameters
        if (params.raw.isNotEmpty()) {
            config = GrinCommandLineConfig.parse(params.raw.toTypedArray())
        }
    }

    override fun start(primaryStage: Stage) {
        scope = MainGrinScope(primaryStage)

        val view = scope.get<ConcatenationView>()
        val scene = Scene(view)

        scope.primaryStage.initWindow(scene, "GrIn")
        scope.primaryStage.show()

        config?.let {
            loadAndShowCharts(it)
        }
    }

    private fun loadAndShowCharts(config: GrinCommandLineConfig) {
        val file = File(config.resultFile!!)
        if (!file.exists()) {
            System.err.println("Result file not found: ${config.resultFile}")
            return
        }

        val metadata = readMetadata(file)
        val columnNames = metadata.columnNames

        val xAxisName = config.xAxis!!
        val xAxisIdx = columnNames.indexOf(xAxisName)
        if (xAxisIdx < 0) {
            System.err.println("X axis column not found: $xAxisName")
            return
        }

        val chartColumns = config.charts.mapIndexedNotNull { idx, name ->
            val colIdx = columnNames.indexOf(name)
            if (colIdx < 0) {
                System.err.println("Column not found: $name")
                null
            } else colIdx to name
        }

        if (chartColumns.isEmpty()) {
            System.err.println("No valid chart columns specified")
            return
        }

        val step = 360.0 / chartColumns.size

        val columnXPoints = MutableList(chartColumns.size) { mutableListOf<Double>() }
        val columnYPoints = MutableList(chartColumns.size) { mutableListOf<Double>() }

        readAllPointsSequence(file).forEach { point ->
            val x = point[xAxisIdx]
            chartColumns.forEachIndexed { i, (colIdx, _) ->
                columnXPoints[i].add(x)
                columnYPoints[i].add(point[colIdx])
            }
        }

        val functions = chartColumns.mapIndexed { i, (_, name) ->
            ConcatenationFunction(
                name = name,
                xPoints = columnXPoints[i].toDoubleArray(),
                yPoints = columnYPoints[i].toDoubleArray(),
                isHide = false,
                functionColor = Color.hsb(step * i, 1.0, 0.8),
                lineSize = 2.0,
                lineType = LineType.POLYNOM
            )
        }

        val yAxisName = chartColumns.joinToString(", ") { it.second }

        val xAxis = ConcatenationAxis(name = xAxisName, direction = Direction.BOTTOM)
        val yAxis = ConcatenationAxis(name = yAxisName, direction = Direction.LEFT)

        val cartesianSpace = CartesianSpace(
            "Simulation Results",
            functions.toMutableList(),
            mutableListOf(),
            xAxis,
            yAxis
        )

        val controller = scope.get<ConcatenationCanvasController>()
        controller.replaceAll(listOf(cartesianSpace), normalizeSpaces = true)
    }

    override fun stop() {
        executor.close()
        scope.scope.close()
        stopKoin()
    }

    private fun Stage.initWindow(scene: Scene, title: String) {
        this.title = title
        this.scene = scene

        icons.add(Image("isma-2016-title.png"))

        isMaximized = false
        height = 600.0
        width = 800.0

        minHeight = 500.0
        minWidth = 600.0
    }
}
