package ru.nstu.grin.concatenation.canvas.handlers

import javafx.event.EventHandler
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.concatenation.canvas.view.ConcatenationChainDrawer
import ru.nstu.grin.concatenation.canvas.model.ContextMenuType
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModelViewModel
import ru.nstu.grin.common.model.PointSettings
import ru.nstu.grin.concatenation.canvas.model.ConcatenationViewModel
import ru.nstu.grin.concatenation.canvas.model.EditMode
import tornadofx.Controller

class ShowPointHandler : EventHandler<MouseEvent>, Controller() {
    private val model: ConcatenationCanvasModelViewModel by inject()
    private val chainDrawer: ConcatenationChainDrawer by inject()
    private val concatenationViewModel: ConcatenationViewModel by inject()

    override fun handle(event: MouseEvent) {
        val editMode = concatenationViewModel.currentEditMode

        if (editMode == EditMode.SCALE) {
            if (event.button == MouseButton.PRIMARY) {
                println("Pressed primary button")
                model.selectionSettings.isSelected = true
                model.selectionSettings.firstPoint = Point(event.x, event.y)
            }
        }

        if (event.button == MouseButton.SECONDARY) {
            println("Set to false")
            model.pointToolTipSettings.isShow = false
            model.pointToolTipSettings.pointsSettings.clear()
        }
        showContextMenu(event)
        if (event.isPrimaryButtonDown.not()) return
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
            nearPoint.x,
            nearPoint.y,
            nearPoint.xGraphic ?: 0.0,
            nearPoint.yGraphic ?: 0.0
        )
        pointToolTipSettings.pointsSettings.add(pointSettings)
        chainDrawer.draw()
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