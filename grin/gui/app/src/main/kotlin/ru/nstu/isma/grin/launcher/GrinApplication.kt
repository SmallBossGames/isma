package ru.nstu.isma.grin.launcher

import javafx.application.Application
import javafx.application.Application.Parameters
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
import ru.nstu.grin.common.model.FunctionModel
import ru.nstu.grin.common.model.Point
import java.io.File
import java.io.FileInputStream

class GrinApplication : Application(), KoinComponent {
    lateinit var scope: MainGrinScope
    private var config: GrinCommandLineConfig? = null

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

        config?.let { loadAndShowCharts(it) }
    }

    private fun loadAndShowCharts(config: GrinCommandLineConfig) {
        val file = File(config.resultFile)
        if (!file.exists()) {
            System.err.println("Result file not found: ${config.resultFile}")
            return
        }

        val metadata = readMetadata(file)
        val columnNames = metadata.columnNames

        if (config.charts.isEmpty()) {
            println("No charts specified. Available columns: ${columnNames.joinToString(", ")}")
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

        val timeIdx = columnNames.indexOf("TIME").takeIf { it >= 0 } ?: 0
        val points = readAllPoints(file, columnNames)

        val step = 360.0 / chartColumns.size

        val functions = chartColumns.mapIndexed { i, (colIdx, name) ->
            val columnPoints = if (timeIdx == colIdx) {
                points.map { Point(it[colIdx], it[colIdx]) }
            } else {
                points.map { Point(it[timeIdx], it[colIdx]) }
            }
            val pts = columnPoints.toList()
            ConcatenationFunction(
                name = name,
                xPoints = pts.map { it.x }.toDoubleArray(),
                yPoints = pts.map { it.y }.toDoubleArray(),
                isHide = false,
                functionColor = Color.hsb(step * i, 1.0, 0.8),
                lineSize = 2.0,
                lineType = LineType.POLYNOM
            )
        }

        val xAxisName = columnNames.getOrNull(timeIdx) ?: "X"
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

    private fun readMetadata(file: File): BinaryMetadataCache {
        val dis = java.io.DataInputStream(java.io.BufferedInputStream(FileInputStream(file)))
        val columnCount = dis.readShort().toInt() and 0xFFFF
        val columnNames = (0 until columnCount).map {
            val len = dis.readShort().toInt() and 0xFFFF
            val bytes = ByteArray(len)
            dis.readFully(bytes)
            String(bytes, Charsets.UTF_8)
        }
        return BinaryMetadataCache(columnNames)
    }

    private fun readAllPoints(file: File, columnNames: List<String>): List<DoubleArray> {
        val points = mutableListOf<DoubleArray>()
        val fis = FileInputStream(file)
        val bis = java.io.BufferedInputStream(fis)
        val dis = java.io.DataInputStream(bis)

        val columnCount = dis.readShort().toInt() and 0xFFFF
        repeat(columnCount) {
            val len = dis.readShort().toInt() and 0xFFFF
            dis.skipBytes(len)
        }

        val totalValues = columnNames.size
        try {
            while (true) {
                val row = DoubleArray(totalValues)
                for (i in 0 until totalValues) {
                    row[i] = dis.readDouble()
                }
                points.add(row)
            }
        } catch (_: java.io.EOFException) {
        }
        return points
    }

    override fun stop() {
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

data class BinaryMetadataCache(
    val columnNames: List<String>,
)