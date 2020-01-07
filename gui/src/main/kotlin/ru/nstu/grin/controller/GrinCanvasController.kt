package ru.nstu.grin.controller

import javafx.stage.StageStyle
import ru.nstu.grin.controller.events.*
import ru.nstu.grin.converters.model.ArrowConverter
import ru.nstu.grin.converters.model.DescriptionConverter
import ru.nstu.grin.converters.model.FunctionConverter
import ru.nstu.grin.file.DrawReader
import ru.nstu.grin.file.DrawWriter
import ru.nstu.grin.model.Direction
import ru.nstu.grin.model.DrawSize
import ru.nstu.grin.model.drawable.Function
import ru.nstu.grin.model.drawable.axis.*
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

    init {
        subscribe<AddArrowEvent> {
            val arrow = ArrowConverter.convert(it.arrowDTO)
            model.drawings.add(arrow)
        }
        subscribe<AddFunctionEvent> {
            val function = FunctionConverter.merge(
                it.functionDTO,
                it.minAxisDelta,
                getStartPointCoef(it.functionDTO.xAxis.direction) * AbstractAxis.WIDTH_AXIS,
                getStartPointCoef(it.functionDTO.yAxis.direction) * AbstractAxis.WIDTH_AXIS
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

    private fun getStartPointCoef(direction: Direction): Int {
        return when (direction) {
            Direction.LEFT -> model.drawings.mapNotNull {
                if (it is Function) {
                    when {
                        it.xAxis is LeftAxis -> {
                            it.xAxis
                        }
                        it.yAxis is LeftAxis -> {
                            it.yAxis
                        }
                    }
                }
            }.count()
            Direction.RIGHT -> {
                model.drawings.mapNotNull {
                    if (it is Function) {
                        when {
                            it.xAxis is RightAxis -> {
                                it.xAxis
                            }
                            it.yAxis is RightAxis -> {
                                it.yAxis
                            }
                        }
                    }
                }.count()
            }
            Direction.TOP -> {
                model.drawings.mapNotNull {
                    if (it is Function) {
                        when {
                            it.xAxis is TopAxis -> {
                                it.xAxis
                            }
                            it.yAxis is TopAxis -> {
                                it.yAxis
                            }
                        }
                    }
                }.count()
            }
            Direction.BOTTOM -> model.drawings.mapNotNull {
                if (it is Function) {
                    when {
                        it.xAxis is BottomAxis -> {
                            it.xAxis
                        }
                        it.yAxis is BottomAxis -> {
                            it.yAxis
                        }
                    }
                }
            }.count()
        }
    }
}