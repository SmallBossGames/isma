package ru.nstu.grin.concatenation.controller

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
import ru.nstu.grin.concatenation.converters.model.CartesianSpaceConverter
import ru.nstu.grin.concatenation.converters.model.ConcatenationAxisConverter
import ru.nstu.grin.concatenation.dto.ConcatenationAxisDTO
import ru.nstu.grin.concatenation.events.ConcatenationFunctionEvent
import ru.nstu.grin.concatenation.events.LoadEvent
import ru.nstu.grin.concatenation.events.SaveEvent
import ru.nstu.grin.concatenation.file.DrawReader
import ru.nstu.grin.concatenation.file.DrawWriter
import ru.nstu.grin.concatenation.model.ChooseFunctionViewModel
import ru.nstu.grin.concatenation.model.Direction
import ru.nstu.grin.concatenation.model.ExistDirection
import ru.nstu.grin.concatenation.model.view.ConcatenationCanvasModelViewModel
import ru.nstu.grin.concatenation.view.ConcatenationCanvas
import ru.nstu.grin.concatenation.view.modal.ChooseFunctionModalView
import ru.nstu.grin.concatenation.view.modal.DescriptionModalView
import tornadofx.*

class ConcatenationCanvasController : Controller() {
    private val model: ConcatenationCanvasModelViewModel by inject()
    private val view: ConcatenationCanvas by inject()
    private val pointCoefCalculator = PointCoefCalculator()

    init {
        subscribe<ConcatenationArrowEvent> {
            val arrow = ArrowConverter.convert(it.arrow)
            model.arrows.add(arrow)
        }
        subscribe<ConcatenationFunctionEvent> {
            val cartesianSpace = it.cartesianSpace
            val found = model.cartesianSpaces.firstOrNull {
                it.xAxis.name == cartesianSpace.xAxis.name
                    && it.yAxis.name == cartesianSpace.yAxis.name
            }
            if (found == null) {
                val xAxis = it.cartesianSpace.xAxis.let { ConcatenationAxisConverter.merge(it, it.getOrder()) }
                val yAxis = it.cartesianSpace.yAxis.let { ConcatenationAxisConverter.merge(it, it.getOrder()) }
                val added = CartesianSpaceConverter.merge(it.cartesianSpace, xAxis, yAxis)
                model.cartesianSpaces.add(added)
            } else {
                TODO("Logic for merge of cartisan spaces")
            }
        }

        subscribe<ConcatenationDescriptionEvent> {
            val description = DescriptionConverter.convert(it.description)
            model.descriptions.add(description)
        }
        subscribe<ConcatenationClearCanvasEvent> {
            view.canvas.graphicsContext2D.clearRect(
                0.0, 0.0,
                SettingsProvider.getCanvasWidth(), SettingsProvider.getCanvasHeight()
            )

        }
        subscribe<SaveEvent> {
            val writer = DrawWriter(it.file)
            writer.write(model.drawings)
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
                    .size - 1
            }
            Direction.RIGHT -> {
                model.cartesianSpaces.filter { it.xAxis.direction == Direction.RIGHT || it.yAxis.direction == Direction.RIGHT }
                    .size - 1
            }
            Direction.TOP -> {
                model.cartesianSpaces.filter { it.xAxis.direction == Direction.TOP || it.yAxis.direction == Direction.TOP }
                    .size - 1
            }
            Direction.BOTTOM -> {
                model.cartesianSpaces.filter { it.xAxis.direction == Direction.BOTTOM || it.yAxis.direction == Direction.BOTTOM }
                    .size - 1
            }
        }
    }

    fun openFunctionModal(
        drawSize: DrawSize,
        xExistDirection: List<ExistDirection>,
        yExistDirection: List<ExistDirection>
    ) {
        find<ChooseFunctionModalView>(
            mapOf(
                ChooseFunctionViewModel::drawSize to drawSize,
                ChooseFunctionViewModel::xExistDirections to xExistDirection,
                ChooseFunctionViewModel::yExistDirections to yExistDirection
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
        view.canvas.graphicsContext2D.clearRect(
            0.0, 0.0,
            SettingsProvider.getCanvasWidth(), SettingsProvider.getCanvasHeight()
        )
        model.arrows.clear()
        model.descriptions.clear()
        model.cartesianSpaces.clear()
    }
}