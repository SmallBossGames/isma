package ru.nstu.grin.concatenation.function.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.nstu.grin.concatenation.axis.converter.ConcatenationAxisConverter
import ru.nstu.grin.concatenation.axis.dto.ConcatenationAxisDTO
import ru.nstu.grin.concatenation.axis.model.Direction
import ru.nstu.grin.concatenation.canvas.controller.MatrixTransformerController
import ru.nstu.grin.concatenation.canvas.converter.CartesianSpaceConverter
import ru.nstu.grin.concatenation.canvas.dto.CartesianSpaceDTO
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.canvas.view.ConcatenationCanvas
import ru.nstu.grin.concatenation.function.converter.ConcatenationFunctionConverter
import ru.nstu.grin.concatenation.function.events.*
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.function.model.DerivativeDetails
import ru.nstu.grin.concatenation.function.model.WaveletDetails
import ru.nstu.grin.concatenation.points.model.PointSettings
import ru.nstu.grin.math.Integration
import ru.nstu.grin.math.IntersectionSearcher
import ru.nstu.grin.model.Function
import ru.nstu.grin.model.MathPoint
import tornadofx.Controller
import java.util.*

class FunctionCanvasService : Controller() {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val model: ConcatenationCanvasModel by inject()
    private val view: ConcatenationCanvas by inject()
    private val matrixTransformer: MatrixTransformerController by inject()

    fun addFunction(cartesianSpace: CartesianSpaceDTO) {
        val found = model.cartesianSpaces.firstOrNull {
            it.xAxis.name == cartesianSpace.xAxis.name
                && it.yAxis.name == cartesianSpace.yAxis.name
        }
        if (found == null) {
            val xAxis = cartesianSpace.xAxis.let { ConcatenationAxisConverter.merge(it, it.getOrder()) }
            val yAxis = cartesianSpace.yAxis.let { ConcatenationAxisConverter.merge(it, it.getOrder()) }
            val added = CartesianSpaceConverter.merge(cartesianSpace, xAxis, yAxis)
            model.cartesianSpaces.add(added)
        } else {
            model.cartesianSpaces.remove(found)

            val functions = cartesianSpace.functions.map { ConcatenationFunctionConverter.convert(it) }
            found.merge(functions)
            model.cartesianSpaces.add(found)
        }
        val firstFun = cartesianSpace.functions.first()
        localizeFunction(LocalizeFunctionEvent(firstFun.id))
    }

    fun copyFunction(originFunction: ConcatenationFunction, newName: String = originFunction.name) {
        val newFunction = originFunction.clone().copy(name = newName)
        val cartesianSpace = model.cartesianSpaces.first { it.functions.contains(originFunction) }
        cartesianSpace.functions.add(newFunction)
        getAllFunctions()
    }

    fun showInterSections(event: ShowIntersectionsEvent) {
        val interactionSearcher = IntersectionSearcher()
        val firstFunc = Function(
            getFunction(event.id).points.map { Pair(it.x.round(), it.y.round()) }
        )
        val firstCartesianSpace = model.cartesianSpaces.first { it.functions.any { it.id == event.id } }
        val secondFunc = Function(
            getFunction(event.secondId).points.map { Pair(it.x.round(), it.y.round()) }
        )
        val secondCartesianSpace = model.cartesianSpaces.first { it.functions.any { it.id == event.secondId } }

        val intersections = interactionSearcher.findIntersections(firstFunc, secondFunc)

        val xArrays = intersections.map { it.first }
        val yArrays = intersections.map { it.second }

        val maxX = xArrays.maxOrNull()!! + INTERSECTION_CORRELATION
        val minX = xArrays.minOrNull()!! - INTERSECTION_CORRELATION
        val maxY = yArrays.maxOrNull()!! + INTERSECTION_CORRELATION
        val minY = yArrays.minOrNull()!! - INTERSECTION_CORRELATION
        firstCartesianSpace.xAxis.settings.min = minX
        firstCartesianSpace.xAxis.settings.max = maxX
        firstCartesianSpace.yAxis.settings.min = minY
        firstCartesianSpace.yAxis.settings.max = maxY
        secondCartesianSpace.xAxis.settings.min = minX
        secondCartesianSpace.xAxis.settings.max = maxX
        secondCartesianSpace.yAxis.settings.min = minY
        secondCartesianSpace.yAxis.settings.max = maxY

        val transformed = intersections
            .map {
                val xGraphic = matrixTransformer.transformUnitsToPixel(
                    it.first,
                    firstCartesianSpace.xAxis.settings,
                    firstCartesianSpace.xAxis.direction
                )
                val yGraphic = matrixTransformer.transformUnitsToPixel(
                    it.second,
                    firstCartesianSpace.yAxis.settings,
                    firstCartesianSpace.yAxis.direction
                )
                PointSettings(
                    firstCartesianSpace.xAxis.settings,
                    firstCartesianSpace.yAxis.settings,
                    xGraphic = xGraphic,
                    yGraphic = yGraphic
                )
            }

        model.pointToolTipSettings.isShow = true
        model.pointToolTipSettings.pointsSettings.addAll(transformed)

        view.redraw()
    }

    fun derivativeFunction(event: DerivativeFunctionEvent) {
        val function = getFunction(event.id)

        function.derivativeDetails = DerivativeDetails(degree = event.degree, type = event.type)

        view.redraw()
    }

    fun waveletFunction(event: WaveletFunctionEvent) {
        val function = getFunction(event.id)

        function.waveletDetails = WaveletDetails(waveletTransformFun = event.waveletTransformFun, waveletDirection = event.waveletDirection)

        view.redraw()
        localizeFunction(LocalizeFunctionEvent(function.id))
    }

    fun calculateIntegral(event: CalculateIntegralEvent) {
        val function = getFunction(event.functionId)
        val min = function.points.minOf { it.x }
        val max = function.points.maxOf { it.x }
        if (min > event.leftBorder) {
            tornadofx.error("Левая граница не может быть меньше минимума функции")
            return
        }
        if (event.rightBorder > max) {
            tornadofx.error("Правя граница не может быть больше максимума функции")
        }
        val integration = Integration()
        val integral =
            integration.trapeze(function.points.filter { it.x > event.leftBorder && it.x < event.rightBorder }
                .map { MathPoint(it.x, it.y) })
        tornadofx.information("Интеграл равен $integral")
    }

    fun localizeFunction(event: LocalizeFunctionEvent) {
        val cartesianSpace = model.cartesianSpaces.first { it.functions.any { it.id == event.id } }
        val function = model.cartesianSpaces.map { it.functions }.flatten().first { it.id == event.id }
        val pixels = function.pixelsToDraw ?: return

        val xPoints = pixels.first.map {
            matrixTransformer.transformPixelToUnits(
                it,
                cartesianSpace.xAxis.settings,
                cartesianSpace.xAxis.direction
            )
        }

        val yPoints = pixels.second.map {
            matrixTransformer.transformPixelToUnits(
                it,
                cartesianSpace.xAxis.settings,
                cartesianSpace.xAxis.direction
            )
        }

        val minY = yPoints.minOrNull() ?: return
        val maxY = yPoints.maxOrNull() ?: return
        val minX = xPoints.minOrNull() ?: return
        val maxX = xPoints.maxOrNull() ?: return

        cartesianSpace.yAxis.settings.min = minY
        cartesianSpace.yAxis.settings.max = maxY

        cartesianSpace.xAxis.settings.min = minX
        cartesianSpace.xAxis.settings.max = maxX

        view.redraw()
    }

    fun updateFunction(event: UpdateFunctionEvent) {
        println("Function updated")
        val function = model.cartesianSpaces.map {
            it.functions
        }.flatten().first { it.id == event.id }

        function.name = event.name
        function.functionColor = event.color
        function.isHide = event.isHide
        function.lineSize = event.lineSize
        function.lineType = event.lineType
        function.mirrorDetails = event.mirroDetails
        view.redraw()
        getAllFunctions()
    }

    fun getFunction(id: UUID): ConcatenationFunction = model.cartesianSpaces.map {
        it.functions
    }.flatten().first { it.id == id }


    fun getAllFunctions() {
        coroutineScope.launch {
            model.reportFunctionsListUpdate()
        }
    }

    fun deleteFunction(function: ConcatenationFunction) {
        model.cartesianSpaces.forEach {
            it.functions.remove(function)
        }
        getAllFunctions()
        view.redraw()
    }

    private fun ConcatenationAxisDTO.getOrder(): Int {
        return when (direction) {
            Direction.LEFT -> {
                model.cartesianSpaces.filter { it.xAxis.direction == Direction.LEFT || it.yAxis.direction == Direction.LEFT }
                    .size
            }
            Direction.RIGHT -> {
                model.cartesianSpaces.filter { it.xAxis.direction == Direction.RIGHT || it.yAxis.direction == Direction.RIGHT }
                    .size
            }
            Direction.TOP -> {
                model.cartesianSpaces.filter { it.xAxis.direction == Direction.TOP || it.yAxis.direction == Direction.TOP }
                    .size
            }
            Direction.BOTTOM -> {
                model.cartesianSpaces.filter { it.xAxis.direction == Direction.BOTTOM || it.yAxis.direction == Direction.BOTTOM }
                    .size
            }
        }
    }

    private fun Double.round(decimals: Int = 2): Double =
        String.format("%.${decimals}f", this).replace(",", ".").toDouble()

    private companion object {
        const val INTERSECTION_CORRELATION = 5.0
    }
}