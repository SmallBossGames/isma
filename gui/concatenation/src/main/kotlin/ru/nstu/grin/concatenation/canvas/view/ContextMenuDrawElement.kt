package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.ContextMenu
import javafx.scene.control.Menu
import javafx.scene.control.MenuItem
import ru.nstu.grin.common.model.DrawSize
import ru.nstu.grin.common.view.ChainDrawElement
import ru.nstu.grin.concatenation.canvas.controller.ConcatenationCanvasController
import ru.nstu.grin.concatenation.canvas.model.ContextMenuType
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModelViewModel
import tornadofx.action

class ContextMenuDrawElement(
    private val contextMenu: ContextMenu,
    private val model: ConcatenationCanvasModelViewModel,
    private val controller: ConcatenationCanvasController,
    private val chainDrawer: ConcatenationChainDrawer
) : ChainDrawElement {
    override fun draw(context: GraphicsContext) {
        contextMenu.items.clear()

        val settings = model.contextMenuSettings
        val stage = model.primaryStage
        when (model.contextMenuSettings.type) {
            ContextMenuType.AXIS -> {
                val axises = model.cartesianSpaces.map {
                    listOf(Pair(it, it.xAxis), Pair(it, it.yAxis))
                }.flatten()
                val cartesianSpace = axises.firstOrNull {
                    it.second.isLocated(settings.xGraphic, settings.yGraphic)
                }?.first ?: return

                val menu = Menu("Логарифмический масштаб")
                val xMenuItem = MenuItem("Включить по x")
                xMenuItem.action {
                    cartesianSpace.xAxis.settings.isLogarithmic = !cartesianSpace.xAxis.settings.isLogarithmic
                    chainDrawer.draw()
                }
                menu.items.add(xMenuItem)

                val yMenuItem = MenuItem("Включить по y")
                yMenuItem.action {
                    cartesianSpace.yAxis.settings.isLogarithmic = !cartesianSpace.yAxis.settings.isLogarithmic
                    chainDrawer.draw()
                }
                menu.items.add(yMenuItem)

                val gridItem = MenuItem("Включить/Выключить сетку")
                gridItem.action {
                    cartesianSpace.isShowGrid = !cartesianSpace.isShowGrid
                    chainDrawer.draw()
                }

                val changeAxis = MenuItem("Изменить ось")
                changeAxis.action {
                    println("Тут мы должны сделать вызов окна изменения")
                }

                contextMenu.items.add(menu)
                contextMenu.items.add(gridItem)
                contextMenu.show(context.canvas, stage.x + settings.xGraphic, stage.y + settings.yGraphic)
            }
            ContextMenuType.MAIN -> {
                val functionItem = MenuItem("Добавить функцию")
                functionItem.action {
                    controller.openFunctionModal(listOf(), listOf())
                }
                val arrowItem = MenuItem("Добавить указатель")
                arrowItem.action {
                    controller.openArrowModal(stage.x + settings.xGraphic, stage.y + settings.yGraphic)
                }
                val descriptionItem = MenuItem("Добавить описание")
                descriptionItem.action {
                    controller.openDescriptionModal(stage.x + settings.xGraphic, stage.y + settings.yGraphic)
                }
                contextMenu.items.addAll(functionItem, arrowItem, descriptionItem)
                contextMenu.show(context.canvas, stage.x + settings.xGraphic, stage.y + settings.yGraphic)
            }
            ContextMenuType.NONE -> {
                contextMenu.hide()
            }
        }
    }
}