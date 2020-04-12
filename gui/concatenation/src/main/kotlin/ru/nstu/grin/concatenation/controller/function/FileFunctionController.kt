package ru.nstu.grin.concatenation.controller.function

import javafx.stage.FileChooser
import ru.nstu.grin.common.common.SettingsProvider
import ru.nstu.grin.common.model.DrawSize
import ru.nstu.grin.concatenation.controller.DeltaMarksGenerator
import ru.nstu.grin.concatenation.controller.DeltaSizeCalculator
import ru.nstu.grin.concatenation.dto.CartesianSpaceDTO
import ru.nstu.grin.concatenation.dto.ConcatenationAxisDTO
import ru.nstu.grin.concatenation.dto.ConcatenationFunctionDTO
import ru.nstu.grin.concatenation.events.ConcatenationFunctionEvent
import ru.nstu.grin.concatenation.events.FileCheckedEvent
import ru.nstu.grin.concatenation.model.PointsViewModel
import ru.nstu.grin.concatenation.model.function.FileFunctionViewModel
import ru.nstu.grin.concatenation.view.modal.PointsView
import tornadofx.Controller
import tornadofx.FileChooserMode

/**
 * @author Konstantin Volivach
 */
class FileFunctionController : Controller() {
    private val model: FileFunctionViewModel by inject()

    fun chooseFile() {
        val files = tornadofx.chooseFile(
            "Файл",
            arrayOf(FileChooser.ExtensionFilter("Путь к файлу", "*.gf")),
            FileChooserMode.Single
        )
        if (files.isEmpty()) {
            tornadofx.error("Файл не был выбран")
            return
        }
        find<PointsView>(
            mapOf(
                PointsViewModel::file to files[0]
            )
        ).openModal()
    }

    fun loadFunctions(drawSize: DrawSize) {
        val points = model.points
        if (points == null) {
            tornadofx.error("Точки не выбраны")
            return
        }

        val delta = DeltaSizeCalculator().calculateDelta(drawSize)
        val deltaMarksGenerator = DeltaMarksGenerator()

        points.forEachIndexed { index, list ->
            val function = ConcatenationFunctionDTO(
                name = "${model.functionName}.$index",
                points = list,
                functionColor = model.functionColor
            )
            val xAxis = ConcatenationAxisDTO(
                name = model.xAxisName,
                backGroundColor = model.xAxisColor,
                delimeterColor = model.xDelimiterColor,
                direction = model.xDirection,
                deltaMarks = deltaMarksGenerator.getDeltaMarks(drawSize, delta, model.xDirection.direction),
                zeroPoint = SettingsProvider.getCanvasWidth() / 2
            )
            val yAxis = ConcatenationAxisDTO(
                name = model.yAxisName,
                backGroundColor = model.yAxisColor,
                delimeterColor = model.yDelimeterColor,
                direction = model.yDirection,
                deltaMarks = deltaMarksGenerator.getDeltaMarks(drawSize, delta, model.yDirection.direction),
                zeroPoint = SettingsProvider.getCanvasHeight() / 2
            )

            val cartesianSpace = CartesianSpaceDTO(
                functions = listOf(function),
                xAxis = xAxis,
                yAxis = yAxis
            )

            fire(ConcatenationFunctionEvent(cartesianSpace, delta))
        }
    }

    private fun <T> List<List<T>>.transpose(): List<List<T>> {
        if (this.isEmpty()) return this

        val result = mutableListOf<List<T>>()
        for (i in this[0].indices) {
            val column = mutableListOf<T>()
            for (row in this) {
                if (row.size <= i) continue
                column.add(row[i])
            }
            result.add(column)
        }
        return result
    }

    init {
        subscribe<FileCheckedEvent> {
            model.points = it.points.transpose()
        }
    }
}