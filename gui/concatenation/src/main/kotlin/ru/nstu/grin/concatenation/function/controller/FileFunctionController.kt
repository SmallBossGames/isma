package ru.nstu.grin.concatenation.function.controller

import javafx.stage.FileChooser
import ru.nstu.grin.common.common.SettingsProvider
import ru.nstu.grin.common.model.DrawSize
import ru.nstu.grin.concatenation.canvas.dto.CartesianSpaceDTO
import ru.nstu.grin.concatenation.axis.dto.ConcatenationAxisDTO
import ru.nstu.grin.concatenation.function.dto.ConcatenationFunctionDTO
import ru.nstu.grin.concatenation.function.events.ConcatenationFunctionEvent
import ru.nstu.grin.concatenation.points.events.FileCheckedEvent
import ru.nstu.grin.concatenation.points.model.AddFunctionsMode
import ru.nstu.grin.concatenation.file.options.model.FileOptionsModel
import ru.nstu.grin.concatenation.function.model.FileFunctionViewModel
import ru.nstu.grin.concatenation.file.options.view.FileOptionsView
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
                val xAxis = createXAxis()
                val yAxis = createYAxis()
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
                fire(
                    ConcatenationFunctionEvent(cartesianSpace)
                )
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
                    val xAxis = createXAxis()
                    val yAxis = createYAxis()

                    val cartesianSpace = CartesianSpaceDTO(
                        functions = listOf(function),
                        xAxis = xAxis,
                        yAxis = yAxis
                    )

                    fire(
                        ConcatenationFunctionEvent(
                            cartesianSpace
                        )
                    )
                }
            }
        }
    }

    private fun createXAxis(): ConcatenationAxisDTO {
        return ConcatenationAxisDTO(
            name = model.xAxisName,
            backGroundColor = model.xAxisColor,
            delimeterColor = model.xDelimiterColor,
            direction = model.xDirection,
            zeroPoint = SettingsProvider.getCanvasWidth() / 2
        )
    }

    private fun createYAxis(): ConcatenationAxisDTO {
        return ConcatenationAxisDTO(
            name = model.yAxisName,
            backGroundColor = model.yAxisColor,
            delimeterColor = model.yDelimeterColor,
            direction = model.yDirection,
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