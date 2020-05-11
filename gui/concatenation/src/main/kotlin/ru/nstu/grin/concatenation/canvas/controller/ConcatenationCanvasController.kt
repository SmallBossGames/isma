package ru.nstu.grin.concatenation.canvas.controller

import javafx.stage.StageStyle
import ru.nstu.grin.common.common.SettingsProvider
import ru.nstu.grin.common.converters.model.ArrowConverter
import ru.nstu.grin.common.converters.model.DescriptionConverter
import ru.nstu.grin.common.events.ConcatenationArrowEvent
import ru.nstu.grin.common.events.ConcatenationClearCanvasEvent
import ru.nstu.grin.common.events.ConcatenationDescriptionEvent
import ru.nstu.grin.common.model.ConcatenationType
import ru.nstu.grin.common.view.modal.ArrowModalView
import ru.nstu.grin.concatenation.canvas.converter.CartesianSpaceConverter
import ru.nstu.grin.concatenation.axis.converter.ConcatenationAxisConverter
import ru.nstu.grin.concatenation.function.converter.ConcatenationFunctionConverter
import ru.nstu.grin.concatenation.axis.dto.ConcatenationAxisDTO
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.axis.model.Direction
import ru.nstu.grin.concatenation.canvas.GenerateUtils
import ru.nstu.grin.concatenation.canvas.events.*
import ru.nstu.grin.concatenation.canvas.model.CanvasModel
import ru.nstu.grin.concatenation.canvas.model.ExistDirection
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModelViewModel
import ru.nstu.grin.concatenation.canvas.view.ConcatenationCanvas
import ru.nstu.grin.concatenation.description.view.DescriptionModalView
import ru.nstu.grin.concatenation.file.DrawReader
import ru.nstu.grin.concatenation.file.DrawWriter
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.function.view.AddFunctionModalView
import tornadofx.*
import java.util.*

/**
 * Разбить по нескольким контроллерам, один для функций, другой для осей и т.д
 */
class ConcatenationCanvasController : Controller() {
    private val canvasModel: CanvasModel by inject()
    private val model: ConcatenationCanvasModelViewModel by inject()
    private val view: ConcatenationCanvas by inject()

    init {
        subscribe<ConcatenationFunctionEvent> { event ->
            addConcatenationFunction(event)
        }
        subscribe<UpdateAxisEvent> {
            updateAxis(it)
            getAllAxises()
        }
        subscribe<UpdateFunctionEvent> {
            updateFunction(it)
            getAllFunctions()
        }
        subscribe<UpdateCartesianEvent> {
            updateCartesian(it)
            getAllCartesianSpaces()
        }
        subscribe<ConcatenationArrowEvent> { event ->
            addArrow(event)
        }
        subscribe<ConcatenationDescriptionEvent> { event ->
            addDescription(event)
        }
        subscribe<ConcatenationClearCanvasEvent> {
            clearCanvas()
        }
        subscribe<SaveEvent> {
            val writer = DrawWriter(it.file)
        }
        subscribe<LoadEvent> {
            val reader = DrawReader()
            val readResult = reader.read(it.file)
            model.arrows.addAll(readResult.arrows)
            model.descriptions.addAll(readResult.descriptions)
            TODO("Add cartesians")
        }
        subscribe<AxisQuery> {
            val axis = findAxisById(it.id)
            val event = GetAxisEvent(axis)
            fire(event)
        }
        subscribe<FunctionQuery> { event ->
            val function = model.cartesianSpaces.map {
                it.functions
            }.flatten().first { it.id == event.id }
            fire(GetFunctionEvent(function))
        }
        subscribe<CartesianQuery> { event ->
            val cartesianSpace = model.cartesianSpaces.first { it.id == event.id }
            fire(GetCartesianEvent(cartesianSpace))
        }
        subscribe<GetAllAxisQuery> {
            getAllAxises()
        }
        subscribe<GetAllFunctionsQuery> {
            getAllFunctions()
        }
        subscribe<GetAllCartesiansQuery> {
            getAllCartesianSpaces()
        }
        subscribe<DeleteFunctionQuery> {
            deleteFunction(it)
            getAllFunctions()
        }
        subscribe<DeleteCartesianSpaceQuery> {
            deleteCartesianSpace(it)
            getAllCartesianSpaces()
        }
//        addFunction()
    }

    private fun getAllCartesianSpaces() {
        val event = GetAllCartesiansEvent(model.cartesianSpaces)
        fire(event)
    }

    private fun getAllAxises() {
        val axises = model.cartesianSpaces.map {
            listOf(it.xAxis, it.yAxis)
        }.flatten()
        val event = GetAllAxisesEvent(axises)
        fire(event)
    }

    private fun getAllFunctions() {
        val functions = model.cartesianSpaces.map {
            it.functions
        }.flatten()
        val event = GetAllFunctionsEvent(functions)
        fire(event)
    }

    private fun findAxisById(id: UUID): ConcatenationAxis {
        return model.cartesianSpaces.map {
            listOf(it.xAxis, it.yAxis)
        }.flatten().first { it.id == id }
    }

    fun addFunction() {
        val (cartesianSpace, cartesianSpace2) = GenerateUtils.generateTwoCartesianSpaces()
        fire(
            ConcatenationFunctionEvent(
                cartesianSpace = cartesianSpace
            )
        )
        fire(
            ConcatenationFunctionEvent(
                cartesianSpace = cartesianSpace2
            )
        )
    }

    fun addArrow(event: ConcatenationArrowEvent) {
        val arrow = ArrowConverter.convert(event.arrow)
        model.arrows.add(arrow)
    }

    fun addDescription(event: ConcatenationDescriptionEvent) {
        val description = DescriptionConverter.convert(event.description)
        model.descriptions.add(description)
    }

    fun addConcatenationFunction(event: ConcatenationFunctionEvent) {
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
    }

    private fun updateAxis(event: UpdateAxisEvent) {
        val axis = model.cartesianSpaces.map {
            listOf(it.xAxis, it.yAxis)
        }.flatten().first { it.id == event.id }

        axis.distanceBetweenMarks = event.distance
        axis.textSize = event.textSize
        axis.font = event.font
        axis.fontColor = event.fontColor
        axis.backGroundColor = event.axisColor
        view.redraw()
    }

    private fun updateCartesian(event: UpdateCartesianEvent) {
        val cartesian = model.cartesianSpaces.first { it.id == event.id }
        cartesian.isShowGrid = event.isShowGrid
        cartesian.name = event.name
        view.redraw()
    }

    private fun updateFunction(event: UpdateFunctionEvent) {
        println("Function updated")
        val function = model.cartesianSpaces.map {
            it.functions
        }.flatten().first { it.id == event.id }

        function.name = event.name
        function.functionColor = event.color
        function.isHide = event.isHide
        function.lineSize = event.lineSize
        function.lineType = event.lineType
        view.redraw()
    }

    private fun deleteCartesianSpace(event: DeleteCartesianSpaceQuery) {
        model.cartesianSpaces.removeIf {
            it.id == event.id
        }
        view.redraw()
    }

    private fun deleteFunction(event: DeleteFunctionQuery) {
        for (cartesianSpace in model.cartesianSpaces) {
            val function = cartesianSpace.functions.firstOrNull { it.id == event.id }
            if (function != null) {
                cartesianSpace.functions.remove(function)
            }
        }
        view.redraw()
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

    fun openFunctionModal(
        xExistDirection: List<ExistDirection>,
        yExistDirection: List<ExistDirection>
    ) {
        find<AddFunctionModalView>(
            mapOf(
                AddFunctionModalView::xExistDirections to xExistDirection,
                AddFunctionModalView::yExistDirections to yExistDirection
            )
        ).openModal()
    }

    fun openArrowModal(x: Double, y: Double) {
        find<ArrowModalView>(
            mapOf(
                ArrowModalView::type to ConcatenationType,
                ArrowModalView::x to x,
                ArrowModalView::y to y
            )
        ).openModal(stageStyle = StageStyle.UTILITY)
    }

    fun openDescriptionModal(x: Double, y: Double) {
        find<DescriptionModalView>(
            mapOf(
                DescriptionModalView::x to x,
                DescriptionModalView::y to y
            )
        ).openModal(stageStyle = StageStyle.UTILITY)
    }

    fun clearCanvas() {
        canvasModel.canvas.graphicsContext2D.clearRect(
            0.0, 0.0,
            SettingsProvider.getCanvasWidth(), SettingsProvider.getCanvasHeight()
        )
        model.arrows.clear()
        model.descriptions.clear()
        model.cartesianSpaces.clear()
    }
}