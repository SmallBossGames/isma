package ru.nstu.grin.concatenation.canvas.handlers

import javafx.event.EventHandler
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import ru.nstu.grin.concatenation.canvas.controller.MatrixTransformerController
import ru.nstu.grin.concatenation.canvas.model.*
import ru.nstu.grin.concatenation.canvas.view.ConcatenationChainDrawer
import ru.nstu.grin.concatenation.canvas.view.ConcatenationView
import tornadofx.Controller
import tornadofx.Scope
import tornadofx.find

class ReleaseMouseHandler : EventHandler<MouseEvent>, Controller() {
    private val model: ConcatenationCanvasModel by inject()
    private val chainDrawer: ConcatenationChainDrawer by inject()
    private val concatenationViewModel: ConcatenationViewModel by inject()
    private val matrixTransformer: MatrixTransformerController by inject()

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
            val selectionSettings = model.selectionSettings

            val area = selectionSettings.area
            if (area < 10.0) {
                model.selectionSettings.reset()
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
                    cartesianSpace.xAxis.settings,
                    cartesianSpace.xAxis.direction
                )
                val maxX = matrixTransformer.transformPixelToUnits(
                    selectionSettings.maxX,
                    cartesianSpace.xAxis.settings,
                    cartesianSpace.xAxis.direction
                )
                cartesianSpace.xAxis.settings.min = minX
                cartesianSpace.xAxis.settings.max = maxX

                // These values are inverted because we count pixels from the top of the canvas
                val minY = matrixTransformer.transformPixelToUnits(
                    selectionSettings.maxY,
                    cartesianSpace.yAxis.settings,
                    cartesianSpace.yAxis.direction
                )
                val maxY = matrixTransformer.transformPixelToUnits(
                    selectionSettings.minY,
                    cartesianSpace.yAxis.settings,
                    cartesianSpace.yAxis.direction
                )
                cartesianSpace.yAxis.settings.min = minY
                cartesianSpace.yAxis.settings.max = maxY
            }
            if (editMode == EditMode.WINDOWED) {
                val initData = InitCanvasData(
                    cartesianSpaces = cartesianSpaces,
                    arrows = model.arrows.toList(),
                    descriptions = model.descriptions.toList()
                )
                val scope = Scope()
                find<ConcatenationView>(
                    scope = scope,
                ).apply {
                    addConcatenationCanvas(initData)
                }.openWindow()
            }

            model.selectionSettings.reset()
        }
        chainDrawer.draw()
    }
}