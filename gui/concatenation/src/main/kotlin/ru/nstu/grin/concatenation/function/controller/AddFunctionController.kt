package ru.nstu.grin.concatenation.function.controller

import ru.nstu.grin.common.common.SettingsProvider
import ru.nstu.grin.common.controller.PointsBuilder
import ru.nstu.grin.common.model.DrawSize
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.concatenation.axis.dto.ConcatenationAxisDTO
import ru.nstu.grin.concatenation.canvas.dto.CartesianSpaceDTO
import ru.nstu.grin.concatenation.function.dto.ConcatenationFunctionDTO
import ru.nstu.grin.concatenation.canvas.events.ConcatenationFunctionEvent
import ru.nstu.grin.concatenation.function.model.*
import ru.nstu.grin.concatenation.points.model.AddFunctionsMode
import tornadofx.Controller
import java.util.*

class AddFunctionController : Controller() {
    private val model: AddFunctionModel by inject()
    private val analyticFunctionModel: AnalyticFunctionModel by inject()
    private val manualFunctionModel: ManualFunctionModel by inject()
    private val fileFunctionModel: FileFunctionModel by inject()
    private val pointsBuilder = PointsBuilder()

    fun addFunction() {
        when (model.inputWay) {
            InputWay.FILE -> addFileFunction()
            InputWay.ANALYTIC -> addAnalyticFunction()
            InputWay.MANUAL -> addManualFunction()
        }
    }

    private fun addAnalyticFunction() {
        val function = ConcatenationFunctionDTO(
            id = UUID.randomUUID(),
            name = model.functionName,
            points = pointsBuilder.buildPoints(
                DrawSize(-1200.0, 1200.0, -1200.0, 1200.0),
                analyticFunctionModel.textFunction,
                analyticFunctionModel.delta
            ).mapIndexedNotNull { index, point ->
                if (index % model.step == 0) {
                    point
                } else {
                    null
                }
            },
            functionColor = model.functionColor,
            lineType = model.functionLineType,
            lineSize = model.functionLineSize.toDouble()
        )

        val cartesianSpace = CartesianSpaceDTO(
            id = UUID.randomUUID(),
            name = model.cartesianSpaceName,
            functions = listOf(function),
            xAxis = ConcatenationAxisDTO(
                id = UUID.randomUUID(),
                name = model.xAxisName,
                backGroundColor = model.xAxisColor,
                delimeterColor = model.xDelimiterColor,
                direction = model.xDirection,
                zeroPoint = SettingsProvider.getCanvasWidth() / 2,
                distanceBetweenMarks = model.xDistanceBetweenMarks.toDouble(),
                textSize = model.xTextSize.toDouble(),
                font = model.xFont
            ),
            yAxis = ConcatenationAxisDTO(
                id = UUID.randomUUID(),
                name = model.yAxisName,
                backGroundColor = model.yAxisColor,
                delimeterColor = model.yDelimeterColor,
                direction = model.yDirection,
                zeroPoint = SettingsProvider.getCanvasHeight() / 2,
                distanceBetweenMarks = model.yDistanceBetweenMarks.toDouble(),
                textSize = model.yTextSize.toDouble(),
                font = model.yFont
            )
        )
        fire(
            ConcatenationFunctionEvent(
                cartesianSpace = cartesianSpace
            )
        )
    }

    private fun addFileFunction() {
        val points = fileFunctionModel.points
        if (points == null) {
            tornadofx.error("Точки не выбраны")
            return
        }

        val addFunctionsMode = fileFunctionModel.addFunctionsMode
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
                        id = UUID.randomUUID(),
                        name = "${model.functionName}.$index",
                        points = list.mapIndexedNotNull { index, point ->
                            if (index % model.step == 0) {
                                point
                            } else {
                                null
                            }
                        },
                        functionColor = model.functionColor,
                        lineType = model.functionLineType,
                        lineSize = model.functionLineSize.toDouble()
                    )
                }
                val cartesianSpace = CartesianSpaceDTO(
                    id = UUID.randomUUID(),
                    name = model.cartesianSpaceName,
                    functions = functions,
                    xAxis = xAxis,
                    yAxis = yAxis
                )
                fire(
                    ConcatenationFunctionEvent(
                        cartesianSpace
                    )
                )
            }
            AddFunctionsMode.ADD_TO_NEW_CARTESIAN_SPACES -> {
                points.forEachIndexed { index, list ->
                    val function = ConcatenationFunctionDTO(
                        id = UUID.randomUUID(),
                        name = "${model.functionName}.$index",
                        points = list.mapIndexedNotNull { index, point ->
                            if (index % model.step == 0) {
                                point
                            } else {
                                null
                            }
                        },
                        functionColor = model.functionColor,
                        lineType = model.functionLineType,
                        lineSize = model.functionLineSize.toDouble()
                    )
                    val xAxis = createXAxis()
                    val yAxis = createYAxis()

                    val cartesianSpace = CartesianSpaceDTO(
                        id = UUID.randomUUID(),
                        name = model.cartesianSpaceName,
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
            id = UUID.randomUUID(),
            name = model.xAxisName,
            backGroundColor = model.xAxisColor,
            delimeterColor = model.xDelimiterColor,
            direction = model.xDirection,
            zeroPoint = SettingsProvider.getCanvasWidth() / 2,
            distanceBetweenMarks = model.xDistanceBetweenMarks.toDouble(),
            font = model.xFont,
            textSize = model.xTextSize.toDouble()
        )
    }

    private fun createYAxis(): ConcatenationAxisDTO {
        return ConcatenationAxisDTO(
            id = UUID.randomUUID(),
            name = model.yAxisName,
            backGroundColor = model.yAxisColor,
            delimeterColor = model.yDelimeterColor,
            direction = model.yDirection,
            zeroPoint = SettingsProvider.getCanvasHeight() / 2,
            distanceBetweenMarks = model.yDistanceBetweenMarks.toDouble(),
            font = model.yFont,
            textSize = model.yTextSize.toDouble()
        )
    }

    private fun parsePoints(): List<Point> {
        val x = manualFunctionModel.xPoints.split(DELIMITER)
        val y = manualFunctionModel.yPoints.split(DELIMITER)
        return x.zip(y) { a, b ->
            Point(a.toDouble(), b.toDouble())
        }
    }

    private fun addManualFunction() {
        val points = parsePoints()

        val function = ConcatenationFunctionDTO(
            id = UUID.randomUUID(),
            name = model.functionName,
            points = points.mapIndexedNotNull { index, point ->
                if (index % model.step == 0) {
                    point
                } else {
                    null
                }
            },
            functionColor = model.functionColor,
            lineType = model.functionLineType,
            lineSize = model.functionLineSize.toDouble()
        )
        val xAxis = ConcatenationAxisDTO(
            id = UUID.randomUUID(),
            name = model.xAxisName,
            backGroundColor = model.xAxisColor,
            delimeterColor = model.xDelimiterColor,
            direction = model.xDirection,
            zeroPoint = SettingsProvider.getCanvasWidth() / 2,
            distanceBetweenMarks = model.xDistanceBetweenMarks.toDouble(),
            font = model.xFont,
            textSize = model.xTextSize.toDouble()
        )
        val yAxis = ConcatenationAxisDTO(
            id = UUID.randomUUID(),
            name = model.yAxisName,
            backGroundColor = model.yAxisColor,
            delimeterColor = model.yDelimeterColor,
            direction = model.yDirection,
            zeroPoint = SettingsProvider.getCanvasHeight() / 2,
            distanceBetweenMarks = model.yDistanceBetweenMarks.toDouble(),
            font = model.yFont,
            textSize = model.yTextSize.toDouble()
        )
        val cartesianSpace = CartesianSpaceDTO(
            id = UUID.randomUUID(),
            name = model.cartesianSpaceName,
            functions = listOf(function),
            xAxis = xAxis,
            yAxis = yAxis
        )

        fire(
            ConcatenationFunctionEvent(cartesianSpace = cartesianSpace)
        )
    }

    private companion object {
        const val DELIMITER = ","
    }
}