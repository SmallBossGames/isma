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
import ru.nstu.grin.concatenation.model.AddFunctionsMode
import ru.nstu.grin.concatenation.model.FileOptionsModel
import ru.nstu.grin.concatenation.model.function.FileFunctionViewModel
import ru.nstu.grin.concatenation.view.FileOptionsView
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
        find<FileOptionsView>(
            mapOf(
                FileOptionsModel::file to files[0]
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

        val addFunctionsMode = model.addFunctionsMode
        if (addFunctionsMode == null) {
            tornadofx.error("Что-то пошло не так, попробуйте сначала")
            return
        }

        when (addFunctionsMode) {
            AddFunctionsMode.ADD_TO_ONE_CARTESIAN_SPACE -> {
                val xAxis = createXAxis(drawSize)
                val yAxis = createYAxis(drawSize)
                val functions = points.mapIndexed { index, list ->
                    ConcatenationFunctionDTO(
                        name = "${model.functionName}.$index",
                        points = list.mapIndexedNotNull { index, point ->
                            if (index % model.step == 0) {
                                point
                            } else {
                                null
                            }
                        },
                        functionColor = model.functionColor
                    )
                }
                val cartesianSpace = CartesianSpaceDTO(
                    functions = functions,
                    xAxis = xAxis,
                    yAxis = yAxis
                )
                fire(ConcatenationFunctionEvent(cartesianSpace, delta))
            }
            AddFunctionsMode.ADD_TO_NEW_CARTESIAN_SPACES -> {
                points.forEachIndexed { index, list ->
                    val function = ConcatenationFunctionDTO(
                        name = "${model.functionName}.$index",
                        points = list.mapIndexedNotNull { index, point ->
                            if (index % model.step == 0) {
                                point
                            } else {
                                null
                            }
                        },
                        functionColor = model.functionColor
                    )
                    val xAxis = createXAxis(drawSize)
                    val yAxis = createYAxis(drawSize)

                    val cartesianSpace = CartesianSpaceDTO(
                        functions = listOf(function),
                        xAxis = xAxis,
                        yAxis = yAxis
                    )

                    fire(ConcatenationFunctionEvent(cartesianSpace, delta))
                }
            }
        }
    }

    private fun createXAxis(drawSize: DrawSize): ConcatenationAxisDTO {
        val delta = DeltaSizeCalculator().calculateDelta(drawSize)
        val deltaMarksGenerator = DeltaMarksGenerator()

        return ConcatenationAxisDTO(
            name = model.xAxisName,
            backGroundColor = model.xAxisColor,
            delimeterColor = model.xDelimiterColor,
            direction = model.xDirection,
            deltaMarks = deltaMarksGenerator.getDeltaMarks(drawSize, delta, model.xDirection.direction),
            zeroPoint = SettingsProvider.getCanvasWidth() / 2
        )
    }

    private fun createYAxis(drawSize: DrawSize): ConcatenationAxisDTO {
        val delta = DeltaSizeCalculator().calculateDelta(drawSize)
        val deltaMarksGenerator = DeltaMarksGenerator()

        return ConcatenationAxisDTO(
            name = model.yAxisName,
            backGroundColor = model.yAxisColor,
            delimeterColor = model.yDelimeterColor,
            direction = model.yDirection,
            deltaMarks = deltaMarksGenerator.getDeltaMarks(drawSize, delta, model.yDirection.direction),
            zeroPoint = SettingsProvider.getCanvasHeight() / 2
        )
    }

    init {
        subscribe<FileCheckedEvent> {
            model.points = it.points
            model.addFunctionsMode = it.addFunctionsMode
        }
    }
}