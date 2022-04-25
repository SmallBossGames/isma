package ru.nstu.grin.concatenation.canvas.handlers

import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.concatenation.axis.extensions.findLocatedAxisOrNull
import ru.nstu.grin.concatenation.canvas.controller.ConcatenationCanvasController
import ru.nstu.grin.concatenation.canvas.model.*
import ru.nstu.grin.concatenation.canvas.view.CartesianCanvasContextMenuController
import ru.nstu.grin.concatenation.canvas.view.ConcatenationChainDrawer
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.points.model.PointSettings

class PressedMouseHandler(
    private val contextMenuDrawElement: CartesianCanvasContextMenuController,
    private val canvasModel: ConcatenationCanvasModel,
    private val canvasController: ConcatenationCanvasController,
    private val canvasViewModel: ConcatenationCanvasViewModel,
    private val chainDrawer: ConcatenationChainDrawer,
    private val editModeViewModel: EditModeViewModel,
) : EventHandler<MouseEvent> {

    override fun handle(event: MouseEvent) {
        contextMenuDrawElement.hide()
        canvasController.unselectAll()

        val editMode = editModeViewModel.currentEditMode

        val updateUi = when(event.button) {
            MouseButton.PRIMARY -> handlePrimaryButtonDown(event, editMode)
            MouseButton.SECONDARY -> handleSecondaryButtonDown(event)
            else -> false
        }

        if(updateUi) {
            chainDrawer.drawUiLayer()
        }
    }

    private fun handlePrimaryButtonDown(event: MouseEvent, editMode: EditMode): Boolean {
        var updateUI = false

        if (editMode == EditMode.SELECTION || editMode == EditMode.MOVE) {
            val description = canvasModel.descriptions.firstOrNull { it.isLocated(event.x, event.y) }
            if(description != null){
                canvasViewModel.selectedDescriptions.add(description)

                updateUI = true
            }

            val function = canvasModel.cartesianSpaces.map { it.functions }.flatten()
                .firstOrNull {
                    val pixels = it.pixelsToDraw ?: return@firstOrNull false

                    ConcatenationFunction.isPixelsNearBy(pixels.first, pixels.second, event.x, event.y)
                }

            if(function != null){
                canvasViewModel.selectedFunctions.add(function)

                updateUI = true
            }
        }

        if ((editMode == EditMode.SCALE || editMode == EditMode.WINDOWED) && !isOnAxis(event)) {
            editModeViewModel.selectionSettings.isFirstPointSelected = true
            editModeViewModel.selectionSettings.firstPoint = Point(event.x, event.y)

            updateUI = true
        }

        if (editMode == EditMode.EDIT) {
            handleEditMode(event)

            updateUI = true
        }

        if (editMode == EditMode.VIEW) {
            handleViewMode(event)

            updateUI = true
        }

        if (editMode == EditMode.MOVE) {
            handleMoveMode(event)

            updateUI = true
        }

        return updateUI
    }

    private fun handleSecondaryButtonDown(event: MouseEvent): Boolean {
        canvasViewModel.pointToolTipSettings.isShow = false
        canvasViewModel.pointToolTipSettings.pointsSettings.clear()

        showContextMenu(event)

        return true
    }

    private fun isOnAxis(event: MouseEvent): Boolean {
        return canvasModel.cartesianSpaces.findLocatedAxisOrNull(event.x, event.y, canvasViewModel) != null
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

        val pointToolTipSettings = canvasViewModel.pointToolTipSettings
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
                    Point(xPoints[index], yPoints[index])
                }

            if (point != null) {
                Triple(it.xAxis, it.yAxis, point)
            } else {
                null
            }
        }.firstOrNull() ?: return
        val (xAxis, yAxis, point) = triple
        editModeViewModel.traceSettings = TraceSettings(
            pressedPoint = point,
            xAxis = xAxis,
            yAxis = yAxis
        )
    }

    private fun handleMoveMode(event: MouseEvent) {
        val description = canvasViewModel.selectedDescriptions.firstOrNull()
        val function = canvasViewModel.selectedFunctions.firstOrNull()
        val cartesian = canvasModel.cartesianSpaces.firstOrNull{ it.functions.contains(function) }

        if (description != null) {
            editModeViewModel.moveSettings = DescriptionMoveSettings(
                description = description,
                currentX = event.x,
                currentY = event.y
            )
        } else if (function != null && cartesian != null) {
            editModeViewModel.moveSettings = FunctionMoveSettings(
                function = function,
                xAxis = cartesian.xAxis,
                yAxis = cartesian.yAxis,
                currentX = event.x,
                currentY = event.y
            )
        }
    }

    private fun showContextMenu(event: MouseEvent) {
        val axis = canvasModel.cartesianSpaces.findLocatedAxisOrNull(event.x, event.y, canvasViewModel)

        if (axis == null) {
            contextMenuDrawElement.showForMain(event.source as Node, event.x, event.y)
        } else {
            contextMenuDrawElement.showForAxis(event.source as Node, axis, event.x, event.y)
        }
    }
}