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
import ru.nstu.grin.concatenation.function.service.FunctionCanvasService
import ru.nstu.grin.concatenation.function.transform.TranslateTransformer

class DraggedHandler(
    private val model: ConcatenationCanvasModel,
    private val canvasViewModel: ConcatenationCanvasViewModel,
    private val chainDrawer: ConcatenationChainDrawer,
    private val editModeViewModel: EditModeViewModel,
    private val matrixTransformer: MatrixTransformer,
    private val functionCanvasService: FunctionCanvasService,
) : EventHandler<MouseEvent> {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val currentCanvasSettings: MutableMap<ConcatenationAxis, DraggedSettings> = mutableMapOf()

    override fun handle(event: MouseEvent) {
        var uiLayerDirty = false

        val editMode = editModeViewModel.currentEditMode
        val isOnAxis = isOnAxis(event)

        if ((editMode == EditMode.SCALE || editMode == EditMode.WINDOWED) && !isOnAxis) {
            if (event.isPrimaryButtonDown) {
                if (!editModeViewModel.selectionSettings.isFirstPointSelected) {
                    editModeViewModel.selectionSettings.firstPoint = Point(event.x, event.y)
                    editModeViewModel.selectionSettings.isFirstPointSelected = true
                } else {
                    editModeViewModel.selectionSettings.secondPoint = Point(event.x, event.y)
                    editModeViewModel.selectionSettings.isSecondPointSelected = true
                }
            }

            if (!event.isPrimaryButtonDown) {
                editModeViewModel.selectionSettings.isFirstPointSelected = false
                editModeViewModel.selectionSettings.isSecondPointSelected = false
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
            val settings = editModeViewModel.moveSettings

            if(settings != null){
                uiLayerDirty = handleMoveMode(event, settings)
            }
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

        canvasViewModel.pointToolTipSettings.isShow = false
        canvasViewModel.pointToolTipSettings.pointsSettings.clear()

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
        val traceSettings = editModeViewModel.traceSettings ?: return
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

    private fun handleMoveMode(event: MouseEvent, moveSettings: IMoveSettings) : Boolean {
        when(moveSettings){
            is FunctionMoveSettings -> {
                val startX = matrixTransformer.transformPixelToUnits(
                    moveSettings.currentX,
                    moveSettings.xAxis.scaleProperties,
                    moveSettings.xAxis.direction
                )
                val startY = matrixTransformer.transformPixelToUnits(
                    moveSettings.currentY,
                    moveSettings.yAxis.scaleProperties,
                    moveSettings.yAxis.direction
                )
                val endX = matrixTransformer.transformPixelToUnits(
                    event.x,
                    moveSettings.xAxis.scaleProperties,
                    moveSettings.xAxis.direction
                )
                val endY = matrixTransformer.transformPixelToUnits(
                    event.y,
                    moveSettings.yAxis.scaleProperties,
                    moveSettings.yAxis.direction
                )
                val x = endX - startX
                val y = endY - startY

                moveSettings.apply {
                    currentX = event.x
                    currentY = event.y
                }

                functionCanvasService.addOrUpdateLastTransformer<TranslateTransformer>(moveSettings.function) { it ->
                    it?.copy(
                        translateX = it.translateX + x, translateY = it.translateY + y
                    )?: TranslateTransformer(
                        translateX = x, translateY = y
                    )
                }

                return false
            }
            is DescriptionMoveSettings -> {
                moveSettings.description.apply {
                    x = event.x
                    y = event.y
                }

                return true
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