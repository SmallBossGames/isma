package ru.nstu.grin.concatenation.canvas.handlers

import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.concatenation.axis.extensions.findLocatedAxisOrNull
import ru.nstu.grin.concatenation.canvas.controller.ConcatenationCanvasController
import ru.nstu.grin.concatenation.canvas.controller.MatrixTransformer
import ru.nstu.grin.concatenation.canvas.model.*
import ru.nstu.grin.concatenation.canvas.view.CartesianCanvasContextMenu
import ru.nstu.grin.concatenation.canvas.view.ConcatenationChainDrawer
import ru.nstu.grin.concatenation.cartesian.model.CartesianSpace
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction

class PressedMouseHandler(
    private val contextMenuDrawElement: CartesianCanvasContextMenu,
    private val canvasModel: ConcatenationCanvasModel,
    private val canvasController: ConcatenationCanvasController,
    private val canvasViewModel: ConcatenationCanvasViewModel,
    private val chainDrawer: ConcatenationChainDrawer,
    private val editModeViewModel: EditModeViewModel,
    private val matrixTransformer: MatrixTransformer,
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
        showContextMenu(event)

        return true
    }

    private fun isOnAxis(event: MouseEvent): Boolean {
        return canvasModel.cartesianSpaces.findLocatedAxisOrNull(event.x, event.y, canvasViewModel) != null
    }

    private fun handleViewMode(event: MouseEvent) {
        var selectedSpace: CartesianSpace? = null
        var bestDistance = Double.POSITIVE_INFINITY
        var bestX = 0.0
        var bestY = 0.0

        for(space in canvasModel.cartesianSpaces){
            for(function in space.functions){
                val (xPixels, yPixels) = function.pixelsToDraw ?: Pair(doubleArrayOf(), doubleArrayOf())

                for (i in xPixels.indices){
                    val distance = Point.estimateDistance(xPixels[i], yPixels[i], event.x, event.y)

                    if(distance < 20.0 && distance < bestDistance){
                        bestDistance = distance
                        selectedSpace = space
                        bestX = xPixels[i]
                        bestY = yPixels[i]
                    }
                }
            }
        }

        if(selectedSpace != null){
            val xScaleProperties = selectedSpace.xAxis.scaleProperties
            val xDirection = selectedSpace.xAxis.direction

            val yScaleProperties = selectedSpace.yAxis.scaleProperties
            val yDirection = selectedSpace.yAxis.direction

            canvasController.addPointDescription(
                selectedSpace,
                matrixTransformer.transformPixelToUnits(bestX, xScaleProperties, xDirection),
                matrixTransformer.transformPixelToUnits(bestY, yScaleProperties, yDirection),
            )
        }
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