package ru.nstu.grin.concatenation.canvas.handlers

import javafx.event.EventHandler
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.concatenation.canvas.view.ConcatenationChainDrawer
import ru.nstu.grin.concatenation.canvas.model.*
import ru.nstu.grin.concatenation.points.model.PointSettings
import tornadofx.Controller

class PressedMouseHandler : EventHandler<MouseEvent>, Controller() {
    private val model: ConcatenationCanvasModelViewModel by inject()
    private val chainDrawer: ConcatenationChainDrawer by inject()
    private val concatenationViewModel: ConcatenationViewModel by inject()

    override fun handle(event: MouseEvent) {
        model.unselectAll()
        val editMode = concatenationViewModel.currentEditMode
        val isOnAxis = isOnAxis(event)
        if ((editMode == EditMode.SELECTION || editMode == EditMode.MOVE) && event.button == MouseButton.PRIMARY) {
            val description = model.descriptions.firstOrNull { it.isLocated(event.x, event.y) }
            description?.isSelected = true

            val function = model.cartesianSpaces.map { it.functions }.flatten()
                .firstOrNull { it.points.any { it.isNearBy(event.x, event.y) } }
            function?.isSelected = true
        }

        if ((editMode == EditMode.SCALE || editMode == EditMode.WINDOWED) && isOnAxis.not()) {
            if (event.button == MouseButton.PRIMARY) {
                println("Pressed primary button")
                model.selectionSettings.isSelected = true
                model.selectionSettings.firstPoint = Point(event.x, event.y)
            }
        }
        if (editMode == EditMode.EDIT && event.button == MouseButton.PRIMARY) {
            handleEditMode(event)
        }
        if (event.button == MouseButton.SECONDARY) {
            println("Set to false")
            model.pointToolTipSettings.isShow = false
            model.pointToolTipSettings.pointsSettings.clear()
        }
        if (editMode == EditMode.VIEW && event.button == MouseButton.PRIMARY) {
            handleViewMode(event)
        }
        if (editMode == EditMode.MOVE && event.button == MouseButton.PRIMARY) {
            handleMoveMode(event)
        }

        showContextMenu(event)
        chainDrawer.draw()
    }

    private fun isOnAxis(event: MouseEvent): Boolean {
        return model.cartesianSpaces.map { listOf(it.xAxis, it.yAxis) }.flatten().any { it.isLocated(event.x, event.y) }
    }

    private fun handleViewMode(event: MouseEvent) {
        val cartesianSpace = model.cartesianSpaces.firstOrNull {
            it.functions.any {
                it.points.any { it.isNearBy(event.x, event.y) }
            }
        } ?: return
        val nearFunction = model.cartesianSpaces.mapNotNull {
            it.functions.firstOrNull {
                it.points.any { it.isNearBy(event.x, event.y) }
            }
        }.firstOrNull() ?: return

        val nearPoint = nearFunction.points.first {
            it.isNearBy(event.x, event.y)
        }
        println("Found nearPoint $nearPoint")

        val pointToolTipSettings = model.pointToolTipSettings
        pointToolTipSettings.isShow = true
        val pointSettings = PointSettings(
            cartesianSpace.xAxis.settings,
            cartesianSpace.yAxis.settings,
            nearPoint.xGraphic ?: 0.0,
            nearPoint.yGraphic ?: 0.0
        )
        pointToolTipSettings.pointsSettings.add(pointSettings)
    }

    private fun handleEditMode(event: MouseEvent) {
        println("Pressed primary button")
        val triple = model.cartesianSpaces.mapNotNull {
            val point = it.functions.filter { it.isHide.not() }.firstOrNull {
                it.points.firstOrNull { it.isNearBy(event.x, event.y) } != null
            }?.points?.first {
                it.isNearBy(event.x, event.y)
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
            .firstOrNull { it.points.any { it.isNearBy(event.x, event.y) } }
        val cartesian =
            model.cartesianSpaces.firstOrNull { it.functions.any { it.points.any { it.isNearBy(event.x, event.y) } } }

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
        if (event.button != MouseButton.SECONDARY) {
            model.contextMenuSettings.type = ContextMenuType.NONE
            return
        }
        val axises = model.cartesianSpaces.map {
            listOf(Pair(it, it.xAxis), Pair(it, it.yAxis))
        }.flatten()
        val cartesianSpace = axises.firstOrNull {
            it.second.isLocated(event.x, event.y)
        }?.first

        if (cartesianSpace == null) {
            model.contextMenuSettings.type = ContextMenuType.MAIN
        } else {
            model.contextMenuSettings.type = ContextMenuType.AXIS
        }
        model.contextMenuSettings.xGraphic = event.x
        model.contextMenuSettings.yGraphic = event.y
        chainDrawer.draw()
    }
}