package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.ContextMenu
import javafx.scene.control.MenuItem
import javafx.stage.Stage
import ru.nstu.grin.common.view.ChainDrawElement
import ru.nstu.grin.concatenation.axis.model.AxisMarkType
import ru.nstu.grin.concatenation.canvas.controller.ConcatenationCanvasController
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.canvas.model.ContextMenuType
import tornadofx.action

class ContextMenuDrawElement(
    private val contextMenu: ContextMenu,
    private val model: ConcatenationCanvasModel,
    private val controller: ConcatenationCanvasController,
    private val chainDrawer: ConcatenationChainDrawer,
    private val stage: Stage,
) : ChainDrawElement {
    override fun draw(context: GraphicsContext, canvasWidth: Double, canvasHeight: Double) {
        contextMenu.items.clear()

        val settings = model.contextMenuSettings
        when (model.contextMenuSettings.type) {
            ContextMenuType.AXIS -> {
                val axises = model.cartesianSpaces.map {
                    listOf(Pair(it, it.xAxis), Pair(it, it.yAxis))
                }.flatten()
                val axis = axises.firstOrNull {
                    it.second.isLocated(
                        settings.xGraphic,
                        settings.yGraphic,
                        canvasWidth,
                        canvasHeight
                    )
                }?.second ?: return
                val cartesianSpace = axises.firstOrNull {
                    it.second.isLocated(
                        settings.xGraphic,
                        settings.yGraphic,
                        canvasWidth,
                        canvasHeight
                    )
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

                // TODO: Disabled until migration to Koin
               /* val changeAxis = MenuItem("Изменить ось")
                changeAxis.action {
                    val axisItem = if (cartesianSpace.xAxis.isLocated(settings.xGraphic, settings.yGraphic, canvasWidth, canvasHeight)) {
                        cartesianSpace.xAxis
                    } else {
                        cartesianSpace.yAxis
                    }

                    val scope = mainGrinScope.get<AxisChangeModalScope>()
                    val view = scope.get<AxisChangeFragment>() { parametersOf(axisItem) }

                    Stage().apply {
                        scene = Scene(view)
                        title = "Change Axis"

                        initModality(Modality.WINDOW_MODAL)

                        setOnCloseRequest {
                            scope.closeScope()
                        }

                        show()
                    }
                }*/

                val hideMenu = MenuItem("Спрятать все функции")
                hideMenu.action {
                    for (function in cartesianSpace.functions) {
                        function.isHide = true
                    }
                    chainDrawer.draw()
                }

                contextMenu.items.add(logMenuItem)
                contextMenu.items.add(gridItem)
                //contextMenu.items.add(changeAxis)
                contextMenu.items.add(hideMenu)
                contextMenu.show(context.canvas, stage.x + settings.xGraphic, stage.y + settings.yGraphic)
            }
            ContextMenuType.MAIN -> {
                val functionItem = MenuItem("Добавить функцию")
                functionItem.action {
                    controller.openFunctionModal(listOf(), listOf())
                }
//                val arrowItem = MenuItem("Добавить указатель")
//                arrowItem.action {
//                    controller.openArrowModal(stage.x + settings.xGraphic, stage.y + settings.yGraphic)
//                }
                val descriptionItem = MenuItem("Добавить описание")
                descriptionItem.action {
                    controller.openDescriptionModal(settings.xGraphic, settings.yGraphic)
                }
                contextMenu.items.addAll(functionItem, descriptionItem)
                contextMenu.show(context.canvas, stage.x + settings.xGraphic, stage.y + settings.yGraphic)
            }
            ContextMenuType.NONE -> {
                contextMenu.hide()
            }
        }
    }
}