package ru.nstu.grin.concatenation.canvas.controller

import javafx.stage.StageStyle
import ru.nstu.grin.common.common.SettingsProvider
import ru.nstu.grin.common.converters.model.ArrowConverter
import ru.nstu.grin.common.converters.model.DescriptionConverter
import ru.nstu.grin.common.events.ConcatenationArrowEvent
import ru.nstu.grin.common.events.ConcatenationClearCanvasEvent
import ru.nstu.grin.common.events.ConcatenationDescriptionEvent
import ru.nstu.grin.common.model.ConcatenationType
import ru.nstu.grin.common.model.DrawSize
import ru.nstu.grin.common.view.modal.ArrowModalView
import ru.nstu.grin.concatenation.canvas.converter.CartesianSpaceConverter
import ru.nstu.grin.concatenation.axis.converter.ConcatenationAxisConverter
import ru.nstu.grin.concatenation.function.converter.ConcatenationFunctionConverter
import ru.nstu.grin.concatenation.axis.dto.ConcatenationAxisDTO
import ru.nstu.grin.concatenation.function.events.ConcatenationFunctionEvent
import ru.nstu.grin.concatenation.canvas.events.LoadEvent
import ru.nstu.grin.concatenation.canvas.events.SaveEvent
import ru.nstu.grin.concatenation.file.DrawReader
import ru.nstu.grin.concatenation.file.DrawWriter
import ru.nstu.grin.concatenation.axis.model.Direction
import ru.nstu.grin.concatenation.canvas.model.ExistDirection
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModelViewModel
import ru.nstu.grin.concatenation.canvas.view.ConcatenationCanvas
import ru.nstu.grin.concatenation.description.view.DescriptionModalView
import ru.nstu.grin.concatenation.function.view.AddFunctionModalView
import tornadofx.*

class ConcatenationCanvasController : Controller() {
    private val model: ConcatenationCanvasModelViewModel by inject()
    private val view: ConcatenationCanvas by inject()

    init {
        subscribe<ConcatenationArrowEvent> {
            val arrow = ArrowConverter.convert(it.arrow)
            model.arrows.add(arrow)
        }
        subscribe<ConcatenationFunctionEvent> { event ->
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

        subscribe<ConcatenationDescriptionEvent> {
            val description = DescriptionConverter.convert(it.description)
            model.descriptions.add(description)
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

    private fun clearCanvas() {
        view.canvas.graphicsContext2D.clearRect(
            0.0, 0.0,
            SettingsProvider.getCanvasWidth(), SettingsProvider.getCanvasHeight()
        )
        model.arrows.clear()
        model.descriptions.clear()
        model.cartesianSpaces.clear()
    }
}