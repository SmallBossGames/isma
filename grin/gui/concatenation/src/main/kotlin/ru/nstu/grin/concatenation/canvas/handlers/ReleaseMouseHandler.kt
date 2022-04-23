package ru.nstu.grin.concatenation.canvas.handlers

import javafx.event.EventHandler
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import ru.nstu.grin.concatenation.canvas.controller.MatrixTransformer
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasViewModel
import ru.nstu.grin.concatenation.canvas.model.ConcatenationViewModel
import ru.nstu.grin.concatenation.canvas.model.EditMode
import ru.nstu.grin.concatenation.canvas.view.ConcatenationChainDrawer

class ReleaseMouseHandler(
    private val model: ConcatenationCanvasModel,
    private val canvasViewModel: ConcatenationCanvasViewModel,
    private val chainDrawer: ConcatenationChainDrawer,
    private val concatenationViewModel: ConcatenationViewModel,
    private val matrixTransformer: MatrixTransformer,
) : EventHandler<MouseEvent> {

    override fun handle(event: MouseEvent) {
        val editMode = concatenationViewModel.currentEditMode

        if (editMode == EditMode.EDIT && event.button == MouseButton.PRIMARY) {
            println("Release primary button")
            model.traceSettings = null
        }

        if (editMode == EditMode.MOVE && event.button == MouseButton.PRIMARY) {
            model.moveSettings = null
        }

        if ((editMode == EditMode.SCALE || editMode == EditMode.WINDOWED) && event.button == MouseButton.PRIMARY) {
            val selectionSettings = canvasViewModel.selectionSettings

            val area = selectionSettings.area
            if (area < 10.0) {
                selectionSettings.reset()
                return
            }

            val cartesianSpaces = if (editMode == EditMode.SCALE) {
                model.cartesianSpaces
            } else {
                model.cartesianSpaces.map { it.clone() }
            }

            for (cartesianSpace in cartesianSpaces) {
                val minX = matrixTransformer.transformPixelToUnits(
                    selectionSettings.minX,
                    cartesianSpace.xAxis.scaleProperties,
                    cartesianSpace.xAxis.direction
                )
                val maxX = matrixTransformer.transformPixelToUnits(
                    selectionSettings.maxX,
                    cartesianSpace.xAxis.scaleProperties,
                    cartesianSpace.xAxis.direction
                )

                cartesianSpace.xAxis.scaleProperties = cartesianSpace.xAxis.scaleProperties.copy(
                    minValue = minX,
                    maxValue = maxX,
                )

                // These values are inverted because we count pixels from the top of the canvas
                val minY = matrixTransformer.transformPixelToUnits(
                    selectionSettings.maxY,
                    cartesianSpace.yAxis.scaleProperties,
                    cartesianSpace.yAxis.direction
                )
                val maxY = matrixTransformer.transformPixelToUnits(
                    selectionSettings.minY,
                    cartesianSpace.yAxis.scaleProperties,
                    cartesianSpace.yAxis.direction
                )

                cartesianSpace.yAxis.scaleProperties = cartesianSpace.yAxis.scaleProperties.copy(
                    minValue = minY,
                    maxValue = maxY,
                )
            }
            //TODO: Disabled until migration to Koin
           /* if (editMode == EditMode.WINDOWED) {
                val initData = InitCanvasData(
                    cartesianSpaces = cartesianSpaces,
                    arrows = model.arrows.toList(),
                    descriptions = model.descriptions.toList()
                )
                val scope = Scope()

                //ConcatenationView(scope, CanvasProjectLoader(scope), initData).openWindow()
            }*/

            selectionSettings.reset()
        }
        chainDrawer.draw()
    }
}