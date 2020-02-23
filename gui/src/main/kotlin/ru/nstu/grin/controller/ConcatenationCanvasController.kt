package ru.nstu.grin.controller

import javafx.stage.StageStyle
import ru.nstu.grin.controller.events.*
import ru.nstu.grin.converters.model.ArrowConverter
import ru.nstu.grin.converters.model.DescriptionConverter
import ru.nstu.grin.converters.model.FunctionConverter
import ru.nstu.grin.file.DrawReader
import ru.nstu.grin.file.DrawWriter
import ru.nstu.grin.model.DrawSize
import ru.nstu.grin.model.ExistDirection
import ru.nstu.grin.model.view.ConcatenationCanvasModelViewModel
import ru.nstu.grin.settings.SettingsProvider
import ru.nstu.grin.view.concatenation.ConcatenationCanvas
import ru.nstu.grin.view.concatenation.modal.ArrowModalView
import ru.nstu.grin.view.concatenation.modal.ChooseFunctionModalView
import ru.nstu.grin.view.concatenation.modal.DescriptionModalView
import tornadofx.*
import ru.nstu.grin.model.drawable.Function
import ru.nstu.grin.model.view.ChooseFunctionViewModel

class ConcatenationCanvasController : Controller() {
    private val model: ConcatenationCanvasModelViewModel by inject()
    private val view: ConcatenationCanvas by inject()
    private val pointCoefCalculator = PointCoefCalculator()

    init {
        subscribe<AddArrowEvent> {
            val arrow = ArrowConverter.convert(it.arrowDTO)
            model.drawings.add(arrow)
        }
        subscribe<AddFunctionEvent> {
            val functionDTO = it.functionDTO
            val xAxises = model.drawings.filterIsInstance<Function>()
                .map { Pair(it.name, it.xAxis) }
            val yAxises = model.drawings.filterIsInstance<Function>()
                .map { Pair(it.name, it.yAxis) }

            val function = FunctionConverter.merge(
                functionDTO,
                it.minAxisDelta,
                pointCoefCalculator.getStartPointCoef(functionDTO.xAxis.direction.direction, model.drawings),
                pointCoefCalculator.getStartPointCoef(functionDTO.yAxis.direction.direction, model.drawings),
                xAxises,
                yAxises
            )
            model.drawings.add(function)
        }

        subscribe<AddDescriptionEvent> {
            val description = DescriptionConverter.convert(it.descriptionDTO)
            model.drawings.add(description)
        }
        subscribe<ClearCanvasEvent> {
            view.canvas.graphicsContext2D.clearRect(0.0, 0.0,
                SettingsProvider.getCanvasWidth(), SettingsProvider.getCanvasHeight())
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
        view.canvas.graphicsContext2D.clearRect(0.0, 0.0,
            SettingsProvider.getCanvasWidth(), SettingsProvider.getCanvasHeight())
        model.drawings.clear()
    }
}