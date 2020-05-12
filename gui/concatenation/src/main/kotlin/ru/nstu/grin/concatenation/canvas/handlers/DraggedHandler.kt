package ru.nstu.grin.concatenation.canvas.handlers

import javafx.event.EventHandler
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.concatenation.canvas.view.ConcatenationChainDrawer
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.canvas.controller.MatrixTransformerController
import ru.nstu.grin.concatenation.canvas.model.DraggedSettings
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModelViewModel
import ru.nstu.grin.concatenation.canvas.model.ConcatenationViewModel
import ru.nstu.grin.concatenation.canvas.model.EditMode
import tornadofx.Controller

class DraggedHandler : EventHandler<MouseEvent>, Controller() {
    private val model: ConcatenationCanvasModelViewModel by inject()
    private val chainDrawer: ConcatenationChainDrawer by inject()
    private val currentCanvasSettings: MutableMap<ConcatenationAxis, DraggedSettings> = mutableMapOf()
    private val concatenationViewModel: ConcatenationViewModel by inject()
    private val matrixTransformer: MatrixTransformerController by inject()

    override fun handle(event: MouseEvent) {
        val editMode = concatenationViewModel.currentEditMode
        val isOnAxis = isOnAxis(event)
        if ((editMode == EditMode.SCALE || editMode == EditMode.WINDOWED) && isOnAxis.not()) {
            if (event.isPrimaryButtonDown) {
                println("Primary button down dragged")
                if (model.selectionSettings.firstPoint.x == -1.0) {
                    println("I'm here mazahaka")
                    model.selectionSettings.isSelected = true
                    model.selectionSettings.firstPoint = Point(event.x, event.y)
                } else {
                    model.selectionSettings.secondPoint = Point(event.x, event.y)
                }
            }
            if (!event.isPrimaryButtonDown) {
                model.selectionSettings.isSelected = false
            }
            chainDrawer.draw()
        }
        if (editMode == EditMode.EDIT && event.button == MouseButton.PRIMARY) {
            handleEditMode(event)
        }

        if (editMode == EditMode.VIEW && event.button == MouseButton.PRIMARY) {
            handleDragged(event)
        }
        chainDrawer.draw()
    }

    private fun isOnAxis(event: MouseEvent): Boolean {
        return model.cartesianSpaces.map { listOf(it.xAxis, it.yAxis) }.flatten().any { it.isLocated(event.x, event.y) }
    }

    private fun handleDragged(event: MouseEvent) {
        model.pointToolTipSettings.isShow = false
        model.pointToolTipSettings.pointsSettings.clear()

        val axises = model.cartesianSpaces.map {
            listOf(it.xAxis, it.yAxis)
        }.flatten()
        val axis = axises.firstOrNull {
            it.isLocated(event.x, event.y)
        } ?: return

        val draggedSettings = getDraggedSettings(axis)

        if (draggedSettings.lastX == -1.0) draggedSettings.lastX = event.x
        if (draggedSettings.lastY == -1.0) draggedSettings.lastY = event.y


        if (axis.isXAxis()) {
            when {
                event.x < draggedSettings.lastX -> {
                    axis.settings.min -= DELTA
                    axis.settings.max -= DELTA
                }
                event.x > draggedSettings.lastX -> {
                    axis.settings.min += DELTA
                    axis.settings.max += DELTA
                }
                else -> {
                }
            }
        } else {
            when {
                event.y < draggedSettings.lastY -> {
                    axis.settings.min -= DELTA
                    axis.settings.max -= DELTA
                }
                event.y > draggedSettings.lastY -> {
                    axis.settings.min += DELTA
                    axis.settings.max += DELTA
                }
                else -> {
                }
            }
        }
        draggedSettings.lastX = event.x
        draggedSettings.lastY = event.y
        currentCanvasSettings[axis] = draggedSettings
    }

    private fun handleEditMode(event: MouseEvent) {
        println("Dragged primary button")
        val traceSettings = model.traceSettings ?: return
        val x = matrixTransformer.transformPixelToUnits(
            event.x,
            traceSettings.xAxis.settings,
            traceSettings.xAxis.direction
        )
        val y = matrixTransformer.transformPixelToUnits(
            event.y,
            traceSettings.yAxis.settings,
            traceSettings.yAxis.direction
        )
        traceSettings.pressedPoint.x = x
        traceSettings.pressedPoint.y = y
        chainDrawer.draw()
    }

    private fun getDraggedSettings(axis: ConcatenationAxis): DraggedSettings {
        return currentCanvasSettings[axis] ?: DraggedSettings()
    }

    private companion object {
        const val DELTA = 0.5
    }
}