package ru.nstu.grin.controller.concatenation

import javafx.stage.StageStyle
import ru.nstu.grin.converters.model.ArrowConverter
import ru.nstu.grin.converters.model.DescriptionConverter
import ru.nstu.grin.converters.model.ConcatenationFunctionConverter
import ru.nstu.grin.events.common.*
import ru.nstu.grin.events.concatenation.*
import ru.nstu.grin.file.DrawReader
import ru.nstu.grin.file.DrawWriter
import ru.nstu.grin.model.ConcatenationType
import ru.nstu.grin.model.DrawSize
import ru.nstu.grin.model.ExistDirection
import ru.nstu.grin.model.view.ConcatenationCanvasModelViewModel
import ru.nstu.grin.settings.SettingsProvider
import ru.nstu.grin.view.concatenation.ConcatenationCanvas
import ru.nstu.grin.view.common.modal.ArrowModalView
import ru.nstu.grin.view.concatenation.modal.ChooseFunctionModalView
import ru.nstu.grin.view.concatenation.modal.DescriptionModalView
import tornadofx.*
import ru.nstu.grin.model.drawable.ConcatenationFunction
import ru.nstu.grin.model.concatenation.ChooseFunctionViewModel

class ConcatenationCanvasController : Controller() {
    private val model: ConcatenationCanvasModelViewModel by inject()
    private val view: ConcatenationCanvas by inject()
    private val pointCoefCalculator = PointCoefCalculator()

    init {
        subscribe<ConcatenationArrowEvent> {
            val arrow = ArrowConverter.convert(it.arrow)
            model.drawings.add(arrow)
        }
        subscribe<ConcatenationFunctionEvent> {
            val functionDTO = it.function
            val xAxises = model.drawings.filterIsInstance<ConcatenationFunction>()
                .map { Pair(it.name, it.xAxis) }
            val yAxises = model.drawings.filterIsInstance<ConcatenationFunction>()
                .map { Pair(it.name, it.yAxis) }

            val function = ConcatenationFunctionConverter.merge(
                functionDTO,
                it.minAxisDelta,
                pointCoefCalculator.getStartPointCoef(functionDTO.xAxis.direction.direction, model.drawings),
                pointCoefCalculator.getStartPointCoef(functionDTO.yAxis.direction.direction, model.drawings),
                xAxises,
                yAxises
            )
            model.drawings.add(function)
        }

        subscribe<ConcatenationDescriptionEvent> {
            val description = DescriptionConverter.convert(it.description)
            model.drawings.add(description)
        }
        subscribe<ConcatenationClearCanvasEvent> {
            view.canvas.graphicsContext2D.clearRect(
                0.0, 0.0,
                SettingsProvider.getCanvasWidth(), SettingsProvider.getCanvasHeight()
            )
            model.drawings.clear()
        }
        subscribe<SaveEvent> {
            val writer = DrawWriter(it.file)
            writer.write(model.drawings)
        }
        subscribe<LoadEvent> {
            val reader = DrawReader()
            val drawings = reader.read(it.file)
            model.drawings.addAll(drawings)
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
        model.drawings.clear()
    }
}