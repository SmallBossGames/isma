package ru.nstu.grin.concatenation.view

import javafx.event.EventHandler
import javafx.scene.control.ContextMenu
import javafx.scene.control.Menu
import javafx.scene.control.MenuItem
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import ru.nstu.grin.concatenation.draw.ConcatenationChainDrawer
import ru.nstu.grin.concatenation.model.ContextMenuType
import ru.nstu.grin.concatenation.model.view.ConcatenationCanvasModelViewModel
import tornadofx.action
import tornadofx.add
import tornadofx.item
import tornadofx.menu

class ShowPointHandler(
    private val model: ConcatenationCanvasModelViewModel,
    private val chainDrawer: ConcatenationChainDrawer
) : EventHandler<MouseEvent> {
    override fun handle(event: MouseEvent) {
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
        pointToolTipSettings.x = nearPoint.x
        pointToolTipSettings.y = nearPoint.y

        pointToolTipSettings.xGraphic = nearPoint.xGraphic ?: 0.0
        pointToolTipSettings.yGraphic = nearPoint.yGraphic ?: 0.0
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
    }
}