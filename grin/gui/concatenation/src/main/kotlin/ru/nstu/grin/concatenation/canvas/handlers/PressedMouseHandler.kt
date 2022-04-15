package ru.nstu.grin.concatenation.canvas.handlers

import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.concatenation.canvas.controller.ConcatenationCanvasController
import ru.nstu.grin.concatenation.canvas.model.*
import ru.nstu.grin.concatenation.canvas.view.ConcatenationChainDrawer
import ru.nstu.grin.concatenation.canvas.view.CartesianCanvasContextMenuController
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.points.model.PointSettings

class PressedMouseHandler(
    private val contextMenuDrawElement: CartesianCanvasContextMenuController,
    private val canvasModel: ConcatenationCanvasModel,
    private val canvasController: ConcatenationCanvasController,
    private val canvasViewModel: CanvasViewModel,
    private val chainDrawer: ConcatenationChainDrawer,
    private val concatenationViewModel: ConcatenationViewModel,
) : EventHandler<MouseEvent> {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    override fun handle(event: MouseEvent) {
        var isUiLayerDirty = false

        closeContextMenu()

        canvasController.unselectAll()
        val editMode = concatenationViewModel.currentEditMode
        val isOnAxis = isOnAxis(event)

        if ((editMode == EditMode.SELECTION || editMode == EditMode.MOVE) && event.button == MouseButton.PRIMARY) {
            val description = canvasModel.descriptions.firstOrNull { it.isLocated(event.x, event.y) }
            description?.isSelected = true

            val function = canvasModel.cartesianSpaces.map { it.functions }.flatten()
                .firstOrNull {
                    val pixels = it.pixelsToDraw ?: return@firstOrNull false

                    return@firstOrNull ConcatenationFunction.isPixelsNearBy(pixels.first, pixels.second, event.x, event.y)
                }
            function?.isSelected = true

            isUiLayerDirty = true
        }

        if ((editMode == EditMode.SCALE || editMode == EditMode.WINDOWED) && !isOnAxis) {
            if (event.button == MouseButton.PRIMARY) {
                canvasModel.selectionSettings.isFirstPointSelected = true
                canvasModel.selectionSettings.firstPoint = Point(event.x, event.y)
            }

            isUiLayerDirty = true
        }

        if (editMode == EditMode.EDIT && event.button == MouseButton.PRIMARY) {
            handleEditMode(event)

            isUiLayerDirty = true
        }

        if (event.button == MouseButton.SECONDARY) {
            canvasModel.pointToolTipSettings.isShow = false
            canvasModel.pointToolTipSettings.pointsSettings.clear()

            isUiLayerDirty = true
        }

        if (editMode == EditMode.VIEW && event.button == MouseButton.PRIMARY) {
            handleViewMode(event)

            isUiLayerDirty = true
        }

        if (editMode == EditMode.MOVE && event.button == MouseButton.PRIMARY) {
            handleMoveMode(event)

            isUiLayerDirty = true
        }

        if (event.button == MouseButton.SECONDARY) {
            showContextMenu(event)
        }

        if(isUiLayerDirty){
            coroutineScope.launch {
                chainDrawer.drawUiLayer()
            }
        }
    }

    private fun isOnAxis(event: MouseEvent): Boolean {
        return canvasModel.cartesianSpaces
            .map { listOf(it.xAxis, it.yAxis) }
            .flatten()
            .any { it.isLocated(event.x, event.y, canvasViewModel.canvasWidth, canvasViewModel.canvasHeight) }
    }

    private fun handleViewMode(event: MouseEvent) {
        val cartesianSpace = canvasModel.cartesianSpaces.firstOrNull {
            it.functions.any {
                val pixels = it.pixelsToDraw ?: return@firstOrNull false

                return@firstOrNull ConcatenationFunction.isPixelsNearBy(pixels.first, pixels.second, event.x, event.y)
            }
        } ?: return
        val nearFunction = canvasModel.cartesianSpaces.mapNotNull {
            it.functions.firstOrNull {
                val pixels = it.pixelsToDraw ?: return@firstOrNull false

                return@firstOrNull ConcatenationFunction.isPixelsNearBy(pixels.first, pixels.second, event.x, event.y)
            }
        }.firstOrNull() ?: return

        val pixels = nearFunction.pixelsToDraw ?: throw java.lang.NullPointerException()

        val nearPointIndex = ConcatenationFunction.indexOfPixelsNearBy(
            pixels.first, pixels.second, event.x, event.y
        )

        val pointToolTipSettings = canvasModel.pointToolTipSettings
        pointToolTipSettings.isShow = true
        val pointSettings = PointSettings(
            cartesianSpace.xAxis.scaleProperties,
            cartesianSpace.yAxis.scaleProperties,
            pixels.first[nearPointIndex],
            pixels.second[nearPointIndex],
        )
        pointToolTipSettings.pointsSettings.add(pointSettings)
    }

    private fun handleEditMode(event: MouseEvent) {
        println("Pressed primary button")
        val triple = canvasModel.cartesianSpaces.mapNotNull {
            val point = it.functions
                .filter { it.isHide.not() }
                .firstOrNull {
                    val pixels = it.pixelsToDraw ?: return@firstOrNull false
                    ConcatenationFunction.isPixelsNearBy(
                        pixels.first, pixels.second, event.x, event.y
                    )
                }?.run {
                    val pixels = pixelsToDraw ?: return@run null
                    val index = ConcatenationFunction.indexOfPixelsNearBy(
                        pixels.first, pixels.second, event.x, event.y
                    )
                    points[index]
                }

            if (point != null) {
                Triple(it.xAxis, it.yAxis, point)
            } else {
                null
            }
        }.firstOrNull() ?: return
        val (xAxis, yAxis, point) = triple
        canvasModel.traceSettings = TraceSettings(
            pressedPoint = point,
            xAxis = xAxis,
            yAxis = yAxis
        )
    }

    private fun handleMoveMode(event: MouseEvent) {
        val description = canvasModel.descriptions.firstOrNull { it.isLocated(event.x, event.y) }
        val function = canvasModel.cartesianSpaces.map { it.functions }.flatten()
            .firstOrNull {
                val pixels = it.pixelsToDraw ?: return@firstOrNull false

                return@firstOrNull ConcatenationFunction.isPixelsNearBy(pixels.first, pixels.second, event.x, event.y)
            }
        val cartesian =
            canvasModel.cartesianSpaces.firstOrNull {
                it.functions.any {
                    val pixels = it.pixelsToDraw ?: return@firstOrNull false

                    return@firstOrNull ConcatenationFunction.isPixelsNearBy(pixels.first, pixels.second, event.x, event.y)
                }
            }

        if (description != null) {
            canvasModel.moveSettings = MoveSettings(
                id = description.id,
                type = MovedElementType.DESCRIPTION,
                pressedX = event.x,
                pressedY = event.y
            )
        } else if (function != null && cartesian != null) {
            canvasModel.moveSettings = MoveSettings(
                id = function.id,
                type = MovedElementType.FUNCTION,
                xAxis = cartesian.xAxis,
                yAxis = cartesian.yAxis,
                pressedX = event.x,
                pressedY = event.y
            )
        }
    }

    private fun closeContextMenu(){
        contextMenuDrawElement.hide()
    }

    private fun showContextMenu(event: MouseEvent) {
        val axes = canvasModel.cartesianSpaces.map {
            listOf(Pair(it, it.xAxis), Pair(it, it.yAxis))
        }.flatten()

        val cartesianSpace = axes.firstOrNull {
            it.second.isLocated(event.x, event.y, canvasViewModel.canvasWidth, canvasViewModel.canvasHeight)
        }?.first

        if (cartesianSpace == null) {
            contextMenuDrawElement.showForMain(event.source as Node, event.x, event.y)
        } else {
            contextMenuDrawElement.showForAxis(event.source as Node, event.x, event.y)
        }
    }
}