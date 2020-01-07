package ru.nstu.grin.controller

import javafx.stage.StageStyle
import ru.nstu.grin.controller.events.*
import ru.nstu.grin.converters.model.ArrowConverter
import ru.nstu.grin.converters.model.DescriptionConverter
import ru.nstu.grin.converters.model.FunctionConverter
import ru.nstu.grin.file.DrawReader
import ru.nstu.grin.file.DrawWriter
import ru.nstu.grin.model.DrawSize
import ru.nstu.grin.model.view.GrinCanvasModelViewModel
import ru.nstu.grin.settings.SettingProvider
import ru.nstu.grin.view.GrinCanvas
import ru.nstu.grin.view.modal.ArrowModalView
import ru.nstu.grin.view.modal.ChooseFunctionModalView
import ru.nstu.grin.view.modal.DescriptionModalView
import tornadofx.*

class GrinCanvasController : Controller() {
    private val model: GrinCanvasModelViewModel by inject()
    private val view: GrinCanvas by inject()
    private val pointCoefCalculator = PointCoefCalculator()

    init {
        subscribe<AddArrowEvent> {
            val arrow = ArrowConverter.convert(it.arrowDTO)
            model.drawings.add(arrow)
        }
        subscribe<AddFunctionEvent> {
            val function = FunctionConverter.merge(
                it.functionDTO,
                it.minAxisDelta,
                pointCoefCalculator.getStartPointCoef(it.functionDTO.xAxis.direction, model.drawings),
                pointCoefCalculator.getStartPointCoef(it.functionDTO.yAxis.direction, model.drawings)
            )
            model.drawings.add(function)
        }
        subscribe<AddDescriptionEvent> {
            val description = DescriptionConverter.convert(it.descriptionDTO)
            model.drawings.add(description)
        }
        subscribe<ClearCanvasEvent> {
            view.canvas.graphicsContext2D.clearRect(0.0, 0.0,
                SettingProvider.getCanvasWidth(), SettingProvider.getCanvasHeight())
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

    fun openFunctionModal(drawSize: DrawSize) {
        find<ChooseFunctionModalView>(
            mapOf(
                ChooseFunctionModalView::drawSize to drawSize
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
            SettingProvider.getCanvasWidth(), SettingProvider.getCanvasHeight())
        model.drawings.clear()
    }
}