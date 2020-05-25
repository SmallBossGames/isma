package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.ContextMenu
import javafx.scene.control.MenuItem
import ru.nstu.grin.common.view.ChainDrawElement
import ru.nstu.grin.concatenation.axis.model.AxisMarkType
import ru.nstu.grin.concatenation.axis.view.AxisChangeFragment
import ru.nstu.grin.concatenation.canvas.controller.ConcatenationCanvasController
import ru.nstu.grin.concatenation.canvas.model.ContextMenuType
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import tornadofx.Scope
import tornadofx.action
import tornadofx.find

class ContextMenuDrawElement(
    private val contextMenu: ContextMenu,
    private val model: ConcatenationCanvasModel,
    private val controller: ConcatenationCanvasController,
    private val chainDrawer: ConcatenationChainDrawer,
    private val scope: Scope
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
                val axis =
                    axises.firstOrNull { it.second.isLocated(settings.xGraphic, settings.yGraphic) }?.second ?: return
                val cartesianSpace = axises.firstOrNull {
                    it.second.isLocated(settings.xGraphic, settings.yGraphic)
                }?.first ?: return

                val logMenuItem = MenuItem("Включить логарифмический масштаб")
                logMenuItem.action {
                    axis.settings.isLogarithmic = !axis.settings.isLogarithmic
                    axis.settings.logarithmBase = 10.0
                    if (axis.axisMarkType == AxisMarkType.LINEAR) {
                        axis.axisMarkType = AxisMarkType.LOGARITHMIC
                    } else {
                        axis.axisMarkType = AxisMarkType.LINEAR
                    }
                    chainDrawer.draw()
                }

                val gridItem = MenuItem("Включить/Выключить сетку")
                gridItem.action {
                    cartesianSpace.isShowGrid = !cartesianSpace.isShowGrid
                    chainDrawer.draw()
                }

                val changeAxis = MenuItem("Изменить ось")
                changeAxis.action {
                    if (cartesianSpace.xAxis.isLocated(settings.xGraphic, settings.yGraphic)) {
                        find<AxisChangeFragment>(
                            scope, mapOf(
                                AxisChangeFragment::axisId.name to cartesianSpace.xAxis.id
                            )
                        ).openModal()
                    } else {
                        find<AxisChangeFragment>(
                            scope, mapOf(
                                AxisChangeFragment::axisId.name to cartesianSpace.yAxis.id
                            )
                        ).openModal()
                    }
                }

                val hideMenu = MenuItem("Спрятать все функции")
                hideMenu.action {
                    for (function in cartesianSpace.functions) {
                        function.isHide = true
                    }
                    chainDrawer.draw()
                }

                contextMenu.items.add(logMenuItem)
                contextMenu.items.add(gridItem)
                contextMenu.items.add(changeAxis)
                contextMenu.items.add(hideMenu)
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
                    controller.openDescriptionModal(settings.xGraphic, settings.yGraphic)
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