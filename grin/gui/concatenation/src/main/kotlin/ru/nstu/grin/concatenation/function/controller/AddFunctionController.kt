package ru.nstu.grin.concatenation.function.controller

import ru.nstu.grin.common.controller.PointsBuilder
import ru.nstu.grin.common.model.DrawSize
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.concatenation.axis.dto.ConcatenationAxisDTO
import ru.nstu.grin.concatenation.canvas.dto.CartesianSpaceDTO
import ru.nstu.grin.concatenation.function.dto.ConcatenationFunctionDTO
import ru.nstu.grin.concatenation.function.model.*
import ru.nstu.grin.concatenation.function.service.FunctionCanvasService
import ru.nstu.grin.concatenation.points.model.AddFunctionsMode
import java.util.*

class AddFunctionController(
    private val functionsCanvasService: FunctionCanvasService,
    private val model: AddFunctionModel,
    private val analyticFunctionModel: AnalyticFunctionModel,
    private val manualFunctionModel: ManualFunctionModel,
    private val fileFunctionModel: FileFunctionModel,
) {
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
                DrawSize(-30.0, 30.0, -100.0, 100.0),
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
            lineSize = model.functionLineSize
        )

        val cartesianSpace = CartesianSpaceDTO(
            id = UUID.randomUUID(),
            name = model.cartesianSpaceName,
            functions = listOf(function),
            xAxis = createXAxis(),
            yAxis = createYAxis()
        )
        functionsCanvasService.addFunction(cartesianSpace)
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
                        points = list.mapIndexedNotNull { i, point ->
                            if (i % model.step == 0) {
                                point
                            } else {
                                null
                            }
                        },
                        functionColor = model.functionColor,
                        lineType = model.functionLineType,
                        lineSize = model.functionLineSize
                    )
                }
                val cartesianSpace = CartesianSpaceDTO(
                    id = UUID.randomUUID(),
                    name = model.cartesianSpaceName,
                    functions = functions,
                    xAxis = xAxis,
                    yAxis = yAxis
                )
                functionsCanvasService.addFunction(cartesianSpace)
            }
            AddFunctionsMode.ADD_TO_NEW_CARTESIAN_SPACES -> {
                points.forEachIndexed { index, list ->
                    val function = ConcatenationFunctionDTO(
                        id = UUID.randomUUID(),
                        name = "${model.functionName}.$index",
                        points = list.mapIndexedNotNull { i, point ->
                            if (i % model.step == 0) {
                                point
                            } else {
                                null
                            }
                        },
                        functionColor = model.functionColor,
                        lineType = model.functionLineType,
                        lineSize = model.functionLineSize
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

                    functionsCanvasService.addFunction(cartesianSpace)
                }
            }
        }
    }

    private fun createXAxis() =
        ConcatenationAxisDTO(
            id = UUID.randomUUID(),
            name = model.xAxis.name,
            backGroundColor = model.xAxis.color,
            delimeterColor = model.xAxis.delimeterColor,
            direction = model.xAxis.direction,
            distanceBetweenMarks = model.xAxis.distanceBetweenMarks,
            textSize = model.xAxis.textSize,
            font = model.xAxis.font
        )

    private fun createYAxis() =
        ConcatenationAxisDTO(
            id = UUID.randomUUID(),
            name = model.yAxis.name,
            backGroundColor = model.yAxis.color,
            delimeterColor = model.yAxis.delimeterColor,
            direction = model.yAxis.direction,
            distanceBetweenMarks = model.yAxis.distanceBetweenMarks,
            textSize = model.yAxis.textSize,
            font = model.yAxis.font
        )

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
            lineSize = model.functionLineSize
        )
        val cartesianSpace = CartesianSpaceDTO(
            id = UUID.randomUUID(),
            name = model.cartesianSpaceName,
            functions = listOf(function),
            xAxis = createXAxis(),
            yAxis = createYAxis()
        )

        functionsCanvasService.addFunction(cartesianSpace)
    }

    private companion object {
        const val DELIMITER = ","
    }
}