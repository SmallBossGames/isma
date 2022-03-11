package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.Node
import javafx.scene.control.ContextMenu
import javafx.scene.control.MenuItem
import ru.nstu.grin.concatenation.axis.controller.AxisListViewController
import ru.nstu.grin.concatenation.axis.model.AxisMarkType
import ru.nstu.grin.concatenation.canvas.controller.ConcatenationCanvasController
import ru.nstu.grin.concatenation.canvas.model.CanvasViewModel
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel

class CartesianCanvasContextMenuController(
    private val chainDrawer: ConcatenationChainDrawer,
    private val model: ConcatenationCanvasModel,
    private val controller: ConcatenationCanvasController,
    private val axisListViewController: AxisListViewController,
    private val canvasViewModel: CanvasViewModel,
) {
    private val contextMenu = ContextMenu()

    fun showForAxis(parent: Node, x: Double, y: Double) {
        val canvasWidth = canvasViewModel.canvasWidth
        val canvasHeight = canvasViewModel.canvasHeight

        val axes = model.cartesianSpaces.map {
            listOf(Pair(it, it.xAxis), Pair(it, it.yAxis))
        }.flatten()

        val axis = axes.firstOrNull {
            it.second.isLocated(x, y, canvasWidth, canvasHeight)
        }?.second ?: return

        val cartesianSpace = axes.firstOrNull {
            it.second.isLocated(x, y, canvasWidth, canvasHeight)
        }?.first ?: return

        contextMenu.items.setAll(
            MenuItem("On/Off Logarithmic").apply {
                setOnAction {
                    axis.settings.isLogarithmic = !axis.settings.isLogarithmic
                    axis.settings.logarithmBase = 10.0
                    if (axis.axisMarkType == AxisMarkType.LINEAR) {
                        axis.axisMarkType = AxisMarkType.LOGARITHMIC
                    } else {
                        axis.axisMarkType = AxisMarkType.LINEAR
                    }
                    chainDrawer.draw()
                }
            },
            MenuItem("On/Off Grid").apply {
                setOnAction {
                    cartesianSpace.isShowGrid = !cartesianSpace.isShowGrid
                    chainDrawer.draw()
                }
            },
            MenuItem("Change Axis").apply {
                setOnAction {
                    val axisItem = if (cartesianSpace.xAxis.isLocated(x, y, canvasWidth, canvasHeight)) {
                        cartesianSpace.xAxis
                    } else {
                        cartesianSpace.yAxis
                    }

                    axisListViewController.editAxis(axisItem)
                }
            },
            MenuItem("Спрятать все функции").apply {
                setOnAction {
                    for (function in cartesianSpace.functions) {
                        function.isHide = true
                    }
                    chainDrawer.draw()
                }
            }
        )

        val contextMenuPosition = parent.localToScreen(x, y)

        contextMenu.show(parent, contextMenuPosition.x, contextMenuPosition.y)
    }

    fun showForMain(parent: Node, x: Double, y: Double){
        val stage = parent.scene.window

        contextMenu.items.setAll(
            MenuItem("Добавить функцию").apply {
                setOnAction {
                    controller.openFunctionModal(stage)
                }
            },
            MenuItem("Добавить указатель").apply {
                setOnAction {
                    controller.openArrowModal(stage.x + x, stage.y + y, stage)
                }
            },
            MenuItem("Добавить описание").apply {
                setOnAction {
                    controller.openDescriptionModal(x, y)
                }
            }
        )

        val contextMenuPosition = parent.localToScreen(x, y)

        contextMenu.show(parent, contextMenuPosition.x, contextMenuPosition.y)
    }

    fun hide() {
        contextMenu.hide()
    }
}