package ru.nstu.grin.concatenation.canvas.handlers

import javafx.event.EventHandler
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import ru.nstu.grin.concatenation.canvas.controller.MatrixTransformerController
import ru.nstu.grin.concatenation.canvas.view.ConcatenationChainDrawer
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModelViewModel
import ru.nstu.grin.concatenation.canvas.model.ConcatenationViewModel
import ru.nstu.grin.concatenation.canvas.model.EditMode
import tornadofx.Controller

class ReleaseMouseHandler : EventHandler<MouseEvent>, Controller() {
    private val model: ConcatenationCanvasModelViewModel by inject()
    private val chainDrawer: ConcatenationChainDrawer by inject()
    private val concatenationViewModel: ConcatenationViewModel by inject()
    private val matrixTransformer: MatrixTransformerController by inject()

    override fun handle(event: MouseEvent) {
        val editMode = concatenationViewModel.currentEditMode
        if (editMode == EditMode.SCALE) {
            if (event.button == MouseButton.PRIMARY) {
                println("Release primary button")

                val selectionSettings = model.selectionSettings
                val area = selectionSettings.getArea()
                if (area < 10.0) {
                    return
                }

                for (cartesianSpace in model.cartesianSpaces) {
                    val minX = matrixTransformer.transformPixelToUnits(
                        selectionSettings.getMinX(),
                        cartesianSpace.xAxis.settings,
                        cartesianSpace.xAxis.direction
                    )
                    val maxX = matrixTransformer.transformPixelToUnits(
                        selectionSettings.getMaxX(),
                        cartesianSpace.xAxis.settings,
                        cartesianSpace.xAxis.direction
                    )
                    println("MinX=$minX and maxX=$maxX")
                    cartesianSpace.xAxis.settings.min = minX
                    cartesianSpace.xAxis.settings.max = maxX

                    val minY = matrixTransformer.transformPixelToUnits(
                        selectionSettings.getMinY(),
                        cartesianSpace.yAxis.settings,
                        cartesianSpace.yAxis.direction
                    )
                    val maxY = matrixTransformer.transformPixelToUnits(
                        selectionSettings.getMaxY(),
                        cartesianSpace.yAxis.settings,
                        cartesianSpace.yAxis.direction
                    )
                    cartesianSpace.yAxis.settings.min = maxY
                    cartesianSpace.yAxis.settings.max = minY
                    println("ySettings=${cartesianSpace.yAxis.settings}")
                }

                model.selectionSettings.dropToDefault()
            }
        }
        chainDrawer.draw()
    }
}