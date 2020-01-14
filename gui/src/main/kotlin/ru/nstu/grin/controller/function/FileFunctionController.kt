package ru.nstu.grin.controller.function

import ru.nstu.grin.controller.DeltaMarksGenerator
import ru.nstu.grin.controller.DeltaSizeCalculator
import ru.nstu.grin.controller.events.AddFunctionEvent
import ru.nstu.grin.dto.AxisDTO
import ru.nstu.grin.dto.FunctionDTO
import ru.nstu.grin.model.DrawSize
import ru.nstu.grin.model.Point
import ru.nstu.grin.model.view.function.FileFunctionViewModel
import tornadofx.Controller
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream

/**
 * @author Konstantin Volivach
 */
class FileFunctionController : Controller() {
    private val model: FileFunctionViewModel by inject()

    private companion object {
        const val DELIMITER = ","
    }

    fun loadFunctions(drawSize: DrawSize) {
        val fileContent = readFile()
        val points = loadAllPoints(fileContent)

        val delta = DeltaSizeCalculator().calculateDelta(drawSize)
        val deltaMarksGenerator = DeltaMarksGenerator()

        points.forEachIndexed { index, list ->
            val functionDTO = FunctionDTO(
                name = "${model.functionName}.$index",
                points = list,
                xAxis = AxisDTO(
                    color = model.xAxisColor,
                    delimeterColor = model.xDelimiterColor,
                    direction = model.xDirection,
                    deltaMarks = deltaMarksGenerator.getDeltaMarks(drawSize, delta, model.xDirection.direction)
                ),
                yAxis = AxisDTO(
                    color = model.yAxisColor,
                    delimeterColor = model.yDelimeterColor,
                    direction = model.yDirection,
                    deltaMarks = deltaMarksGenerator.getDeltaMarks(drawSize, delta, model.yDirection.direction)
                ),
                functionColor = model.functionColor
            )
            fire(AddFunctionEvent(functionDTO, delta))
        }
    }

    private fun loadAllPoints(fileContent: String): List<List<Point>> {
        val lines = fileContent.split("\n")
        val listOfPoints = mutableListOf<List<Point>>()
        for (i in 0 until lines.size - 1 step 2) {
            val x = lines[i].split(DELIMITER)
            val y = lines[i + 1].split(DELIMITER)
            val zipped = x.zip(y) { a, b ->
                Point(a.toDouble(), b.toDouble())
            }
            listOfPoints.add(zipped)
        }
        return listOfPoints
    }

    private fun readFile(): String {
        val file = File(model.filePath)
        val inputStream = FileInputStream(file)
        val bufferedReader = BufferedInputStream(inputStream)
        val array = bufferedReader.readBytes()
        return String(array)
    }
}