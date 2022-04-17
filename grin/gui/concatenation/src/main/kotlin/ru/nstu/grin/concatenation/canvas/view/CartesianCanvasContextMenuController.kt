package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.Node
import javafx.scene.control.ContextMenu
import javafx.scene.control.MenuItem
import ru.nstu.grin.concatenation.axis.controller.AxisListViewController
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.canvas.controller.ConcatenationCanvasController
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel

class CartesianCanvasContextMenuController(
    private val chainDrawer: ConcatenationChainDrawer,
    private val model: ConcatenationCanvasModel,
    private val controller: ConcatenationCanvasController,
    private val axisListViewController: AxisListViewController,
) {
    private val contextMenu = ContextMenu()

    fun showForAxis(parent: Node, axis: ConcatenationAxis, x: Double, y: Double) {
        val cartesianSpace = model.cartesianSpaces.firstOrNull { it.axes.contains(axis) } ?: return

        contextMenu.items.setAll(
/*            MenuItem("On/Off Logarithmic").apply {
                setOnAction {
                    axis.settings.isLogarithmic = !axis.settings.isLogarithmic
                    axis.settings.logarithmBase = 10.0
                    if (axis.axisMarkType == AxisScalingType.LINEAR) {
                        axis.axisMarkType = AxisScalingType.LOGARITHMIC
                    } else {
                        axis.axisMarkType = AxisScalingType.LINEAR
                    }
                    chainDrawer.draw()
                }
            },*/
            MenuItem("On/Off Grid").apply {
                setOnAction {
                    cartesianSpace.isShowGrid = !cartesianSpace.isShowGrid
                    chainDrawer.draw()
                }
            },
            MenuItem("Change Axis").apply {
                setOnAction {
                    axisListViewController.editAxis(axis)
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