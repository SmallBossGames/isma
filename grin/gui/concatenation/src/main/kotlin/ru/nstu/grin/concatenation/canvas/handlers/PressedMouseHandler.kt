package ru.nstu.grin.concatenation.canvas.handlers

import javafx.event.EventHandler
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.concatenation.canvas.view.ConcatenationChainDrawer
import ru.nstu.grin.concatenation.canvas.model.*
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.points.model.PointSettings
import tornadofx.Controller

class PressedMouseHandler : EventHandler<MouseEvent>, Controller() {
    private val model: ConcatenationCanvasModel by inject()
    private val canvasViewModel: CanvasViewModel by inject()
    private val chainDrawer: ConcatenationChainDrawer by inject()
    private val concatenationViewModel: ConcatenationViewModel by inject()

    override fun handle(event: MouseEvent) {
        var isUiLayerDirty = false

        model.unselectAll()
        val editMode = concatenationViewModel.currentEditMode
        val isOnAxis = isOnAxis(event)

        if ((editMode == EditMode.SELECTION || editMode == EditMode.MOVE) && event.button == MouseButton.PRIMARY) {
            val description = model.descriptions.firstOrNull { it.isLocated(event.x, event.y) }
            description?.isSelected = true

            val function = model.cartesianSpaces.map { it.functions }.flatten()
                .firstOrNull {
                    val pixels = it.pixelsToDraw ?: return@firstOrNull false

                    return@firstOrNull ConcatenationFunction.isPixelsNearBy(pixels.first, pixels.second, event.x, event.y)
                }
            function?.isSelected = true

            isUiLayerDirty = true
        }

        if ((editMode == EditMode.SCALE || editMode == EditMode.WINDOWED) && !isOnAxis) {
            if (event.button == MouseButton.PRIMARY) {
                model.selectionSettings.isFirstPointSelected = true
                model.selectionSettings.firstPoint = Point(event.x, event.y)
            }

            isUiLayerDirty = true
        }

        if (editMode == EditMode.EDIT && event.button == MouseButton.PRIMARY) {
            handleEditMode(event)

            isUiLayerDirty = true
        }

        if (event.button == MouseButton.SECONDARY) {
            model.pointToolTipSettings.isShow = false
            model.pointToolTipSettings.pointsSettings.clear()

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

            isUiLayerDirty = true
        } else {
            model.contextMenuSettings.type = ContextMenuType.NONE
        }

        if(isUiLayerDirty){
            chainDrawer.drawUiLayer()
        }
    }

    private fun isOnAxis(event: MouseEvent): Boolean {
        return model.cartesianSpaces
            .map { listOf(it.xAxis, it.yAxis) }
            .flatten()
            .any { it.isLocated(event.x, event.y, canvasViewModel.canvasWidth, canvasViewModel.canvasHeight) }
    }

    private fun handleViewMode(event: MouseEvent) {
        val cartesianSpace = model.cartesianSpaces.firstOrNull {
            it.functions.any {
                val pixels = it.pixelsToDraw ?: return@firstOrNull false

                return@firstOrNull ConcatenationFunction.isPixelsNearBy(pixels.first, pixels.second, event.x, event.y)
            }
        } ?: return
        val nearFunction = model.cartesianSpaces.mapNotNull {
            it.functions.firstOrNull {
                val pixels = it.pixelsToDraw ?: return@firstOrNull false

                return@firstOrNull ConcatenationFunction.isPixelsNearBy(pixels.first, pixels.second, event.x, event.y)
            }
        }.firstOrNull() ?: return

        val pixels = nearFunction.pixelsToDraw ?: throw java.lang.NullPointerException()

        val nearPointIndex = ConcatenationFunction.indexOfPixelsNearBy(
            pixels.first, pixels.second, event.x, event.y
        )

        val pointToolTipSettings = model.pointToolTipSettings
        pointToolTipSettings.isShow = true
        val pointSettings = PointSettings(
            cartesianSpace.xAxis.settings,
            cartesianSpace.yAxis.settings,
            pixels.first[nearPointIndex],
            pixels.second[nearPointIndex],
        )
        pointToolTipSettings.pointsSettings.add(pointSettings)
    }

    private fun handleEditMode(event: MouseEvent) {
        println("Pressed primary button")
        val triple = model.cartesianSpaces.mapNotNull {
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
        model.traceSettings = TraceSettings(
            pressedPoint = point,
            xAxis = xAxis,
            yAxis = yAxis
        )
    }

    private fun handleMoveMode(event: MouseEvent) {
        val description = model.descriptions.firstOrNull { it.isLocated(event.x, event.y) }
        val function = model.cartesianSpaces.map { it.functions }.flatten()
            .firstOrNull {
                val pixels = it.pixelsToDraw ?: return@firstOrNull false

                return@firstOrNull ConcatenationFunction.isPixelsNearBy(pixels.first, pixels.second, event.x, event.y)
            }
        val cartesian =
            model.cartesianSpaces.firstOrNull {
                it.functions.any {
                    val pixels = it.pixelsToDraw ?: return@firstOrNull false

                    return@firstOrNull ConcatenationFunction.isPixelsNearBy(pixels.first, pixels.second, event.x, event.y)
                }
            }

        if (description != null) {
            model.moveSettings = MoveSettings(
                id = description.id,
                type = MovedElementType.DESCRIPTION,
                pressedX = event.x,
                pressedY = event.y
            )
        } else if (function != null && cartesian != null) {
            model.moveSettings = MoveSettings(
                id = function.id,
                type = MovedElementType.FUNCTION,
                xAxis = cartesian.xAxis,
                yAxis = cartesian.yAxis,
                pressedX = event.x,
                pressedY = event.y
            )
        }
    }

    private fun showContextMenu(event: MouseEvent) {
        val axises = model.cartesianSpaces.map {
            listOf(Pair(it, it.xAxis), Pair(it, it.yAxis))
        }.flatten()

        val cartesianSpace = axises.firstOrNull {
            it.second.isLocated(event.x, event.y, canvasViewModel.canvasWidth, canvasViewModel.canvasHeight)
        }?.first

        if (cartesianSpace == null) {
            model.contextMenuSettings.type = ContextMenuType.MAIN
        } else {
            model.contextMenuSettings.type = ContextMenuType.AXIS
        }

        model.contextMenuSettings.xGraphic = event.x
        model.contextMenuSettings.yGraphic = event.y
    }
}