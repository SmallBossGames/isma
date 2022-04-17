package ru.nstu.grin.concatenation.canvas.handlers

import javafx.event.EventHandler
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.axis.extensions.findLocatedAxisOrNull
import ru.nstu.grin.concatenation.axis.model.Direction
import ru.nstu.grin.concatenation.canvas.controller.MatrixTransformer
import ru.nstu.grin.concatenation.canvas.model.*
import ru.nstu.grin.concatenation.canvas.view.ConcatenationChainDrawer

class DraggedHandler(
    private val model: ConcatenationCanvasModel,
    private val canvasViewModel: CanvasViewModel,
    private val chainDrawer: ConcatenationChainDrawer,
    private val concatenationViewModel: ConcatenationViewModel,
    private val matrixTransformer: MatrixTransformer,
) : EventHandler<MouseEvent> {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val currentCanvasSettings: MutableMap<ConcatenationAxis, DraggedSettings> = mutableMapOf()

    override fun handle(event: MouseEvent) {
        var uiLayerDirty = false

        val editMode = concatenationViewModel.currentEditMode
        val isOnAxis = isOnAxis(event)

        if ((editMode == EditMode.SCALE || editMode == EditMode.WINDOWED) && !isOnAxis) {
            if (event.isPrimaryButtonDown) {
                if (!model.selectionSettings.isFirstPointSelected) {
                    model.selectionSettings.firstPoint = Point(event.x, event.y)
                    model.selectionSettings.isFirstPointSelected = true
                } else {
                    model.selectionSettings.secondPoint = Point(event.x, event.y)
                    model.selectionSettings.isSecondPointSelected = true
                }
            }

            if (!event.isPrimaryButtonDown) {
                model.selectionSettings.isFirstPointSelected = false
                model.selectionSettings.isSecondPointSelected = false
            }

            uiLayerDirty = true
        }

        if (editMode == EditMode.EDIT && event.button == MouseButton.PRIMARY) {
            handleEditMode(event)

            uiLayerDirty = true
        }

        if (editMode == EditMode.VIEW && event.button == MouseButton.PRIMARY) {
            handleDragged(event)

            uiLayerDirty = true
        }

        if (editMode == EditMode.MOVE && event.button == MouseButton.PRIMARY) {
            handleMoveMode(event)

            uiLayerDirty = true
        }

        if (uiLayerDirty){
            coroutineScope.launch {
                chainDrawer.drawUiLayer()
            }
        }
    }

    private fun isOnAxis(event: MouseEvent): Boolean {
        return model.cartesianSpaces.findLocatedAxisOrNull(event.x, event.y, canvasViewModel) != null
    }

    private fun handleDragged(event: MouseEvent) {
        if(isOnAxis(event)){
            return
        }

        model.pointToolTipSettings.isShow = false
        model.pointToolTipSettings.pointsSettings.clear()

        val axis = model.cartesianSpaces.findLocatedAxisOrNull(event.x, event.y, canvasViewModel) ?: return

        val draggedSettings = getDraggedSettings(axis)

        if (draggedSettings.lastX == -1.0) draggedSettings.lastX = event.x
        if (draggedSettings.lastY == -1.0) draggedSettings.lastY = event.y

        val scaleProperties = axis.scaleProperties

        when(axis.direction) {
            Direction.LEFT, Direction.RIGHT -> {
                when {
                    event.y < draggedSettings.lastY -> {
                        axis.scaleProperties = axis.scaleProperties.copy(
                            minValue = scaleProperties.minValue - DELTA,
                            maxValue = scaleProperties.maxValue - DELTA,
                        )
                    }
                    event.y > draggedSettings.lastY -> {
                        axis.scaleProperties = axis.scaleProperties.copy(
                            minValue = scaleProperties.minValue + DELTA,
                            maxValue = scaleProperties.maxValue + DELTA,
                        )
                    }
                    else -> throw IllegalStateException()
                }
            }
            Direction.TOP, Direction.BOTTOM -> {
                when {
                    event.x < draggedSettings.lastX -> {
                        axis.scaleProperties = axis.scaleProperties.copy(
                            minValue = scaleProperties.minValue - DELTA,
                            maxValue = scaleProperties.maxValue - DELTA,
                        )
                    }
                    event.x > draggedSettings.lastX -> {
                        axis.scaleProperties = axis.scaleProperties.copy(
                            minValue = scaleProperties.minValue + DELTA,
                            maxValue = scaleProperties.maxValue + DELTA,
                        )
                    }
                    else -> throw IllegalStateException()
                }
            }
        }

        draggedSettings.lastX = event.x
        draggedSettings.lastY = event.y
        currentCanvasSettings[axis] = draggedSettings
    }

    private fun handleEditMode(event: MouseEvent) {
        val traceSettings = model.traceSettings ?: return
        val x = matrixTransformer.transformPixelToUnits(
            event.x,
            traceSettings.xAxis.scaleProperties,
            traceSettings.xAxis.direction
        )
        val y = matrixTransformer.transformPixelToUnits(
            event.y,
            traceSettings.yAxis.scaleProperties,
            traceSettings.yAxis.direction
        )
        traceSettings.pressedPoint.x = x
        traceSettings.pressedPoint.y = y
    }

    private fun handleMoveMode(event: MouseEvent) {
        val moveSettings = model.moveSettings ?: return
        when (moveSettings.type) {
            MovedElementType.FUNCTION -> {
                val xAxis = requireNotNull(moveSettings.xAxis) { "Function move event can't have null axises" }
                val yAxis = requireNotNull(moveSettings.yAxis) { "Function move event can't have null axises" }
                val xLeft = matrixTransformer.transformPixelToUnits(
                    moveSettings.pressedX,
                    xAxis.scaleProperties,
                    xAxis.direction
                )
                val yLeft = matrixTransformer.transformPixelToUnits(
                    moveSettings.pressedY,
                    yAxis.scaleProperties,
                    yAxis.direction
                )
                val xRight = matrixTransformer.transformPixelToUnits(
                    event.x,
                    xAxis.scaleProperties,
                    xAxis.direction
                )
                val yRight = matrixTransformer.transformPixelToUnits(
                    event.y,
                    yAxis.scaleProperties,
                    yAxis.direction
                )
                val x = xLeft - xRight
                val y = yLeft - yRight
                println("Moved x=$x y=$y")
                val function =
                    model.cartesianSpaces.map { it.functions }.flatten().firstOrNull { it.id == moveSettings.id }
                        ?: return
                function.points.forEach {
                    it.x -= x
                    it.y -= y
                }
            }
            MovedElementType.DESCRIPTION -> {
                val description = model.descriptions.firstOrNull { it.id == moveSettings.id } ?: return
                description.x = event.x
                description.y = event.y
            }
        }
    }

    private fun getDraggedSettings(axis: ConcatenationAxis): DraggedSettings {
        return currentCanvasSettings[axis] ?: DraggedSettings()
    }

    private companion object {
        const val DELTA = 0.5
    }
}