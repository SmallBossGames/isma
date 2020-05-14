package ru.nstu.grin.concatenation.function.service

import ru.nstu.grin.common.model.PointSettings
import ru.nstu.grin.concatenation.axis.converter.ConcatenationAxisConverter
import ru.nstu.grin.concatenation.axis.dto.ConcatenationAxisDTO
import ru.nstu.grin.concatenation.axis.model.Direction
import ru.nstu.grin.concatenation.canvas.controller.MatrixTransformerController
import ru.nstu.grin.concatenation.canvas.converter.CartesianSpaceConverter
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModelViewModel
import ru.nstu.grin.concatenation.canvas.view.ConcatenationCanvas
import ru.nstu.grin.concatenation.function.converter.ConcatenationFunctionConverter
import ru.nstu.grin.concatenation.function.events.*
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.function.model.MirrorDetails
import ru.nstu.grin.math.IntersectionSearcher
import ru.nstu.grin.model.Function
import tornadofx.Controller
import java.util.*

class FunctionCanvasService : Controller() {
    private val model: ConcatenationCanvasModelViewModel by inject()
    private val view: ConcatenationCanvas by inject()
    private val matrixTransformer: MatrixTransformerController by inject()

    fun addFunction(event: ConcatenationFunctionEvent) {
        val cartesianSpace = event.cartesianSpace
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
        val firstFun = event.cartesianSpace.functions.first()
        localizeFunction(LocalizeFunctionEvent(firstFun.id))
    }

    fun copyFunction(event: FunctionCopyQuery) {
        val oldFunction = model.cartesianSpaces.map { it.functions }.flatten().first { it.id == event.id }
        val newFunction = oldFunction.clone().copy(id = UUID.randomUUID(), name = event.name)
        val cartesianSpace = model.cartesianSpaces.first { it.functions.firstOrNull { it.id == event.id } != null }
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

        val maxX = xArrays.max()!! + INTERSECTION_CORRELATION
        val minX = xArrays.min()!! - INTERSECTION_CORRELATION
        val maxY = yArrays.max()!! + INTERSECTION_CORRELATION
        val minY = yArrays.min()!! - INTERSECTION_CORRELATION
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
                    x = it.first,
                    y = it.second,
                    xGraphic = xGraphic,
                    yGraphic = yGraphic
                )
            }

        model.pointToolTipSettings.isShow = true
        model.pointToolTipSettings.pointsSettings.addAll(transformed)

        view.redraw()
    }

    private fun Double.round(decimals: Int = 2): Double =
        String.format("%.${decimals}f", this).replace(",", ".").toDouble()

    fun localizeFunction(event: LocalizeFunctionEvent) {
        val cartesianSpace = model.cartesianSpaces.first { it.functions.any { it.id == event.id } }
        val function = model.cartesianSpaces.map { it.functions }.flatten().first { it.id == event.id }
        val xPoints = function.points.map { it.x }
        val yPoints = function.points.map { it.y }

        val minY = yPoints.min() ?: return
        val maxY = yPoints.max() ?: return
        val minX = xPoints.min() ?: return
        val maxX = xPoints.max() ?: return

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
        function.replaceMirrorDetails(event.mirroDetails)
        view.redraw()
        getAllFunctions()
    }

    fun getFunction(id: UUID): ConcatenationFunction = model.cartesianSpaces.map {
        it.functions
    }.flatten().first { it.id == id }


    fun getAllFunctions() {
        val functions = model.cartesianSpaces.map {
            it.functions
        }.flatten()
        val event = GetAllFunctionsEvent(functions)
        fire(event)
    }

    fun deleteFunction(event: DeleteFunctionQuery) {
        for (cartesianSpace in model.cartesianSpaces) {
            val function = cartesianSpace.functions.firstOrNull { it.id == event.id }
            if (function != null) {
                cartesianSpace.functions.remove(function)
            }
        }
        view.redraw()
        getAllFunctions()
    }

    private fun ConcatenationAxisDTO.getOrder(): Int {
        return when (direction.direction) {
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

    private companion object {
        const val INTERSECTION_CORRELATION = 5.0
    }
}