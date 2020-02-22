package ru.nstu.grin.view

import javafx.event.EventHandler
import javafx.event.EventTarget
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.canvas.Canvas
import javafx.scene.control.ContextMenu
import javafx.scene.control.Control
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Priority
import ru.nstu.grin.controller.ConcatenationCanvasController
import ru.nstu.grin.extensions.drawListener
import ru.nstu.grin.model.DrawSize
import ru.nstu.grin.model.ExistDirection
import ru.nstu.grin.model.drawable.Function
import ru.nstu.grin.model.drawable.axis.AbstractAxis
import ru.nstu.grin.model.view.ConcatenationCanvasModelViewModel
import ru.nstu.grin.settings.SettingsProvider
import tornadofx.*

class ConcatenationCanvas : View() {
    private val model: ConcatenationCanvasModelViewModel by inject()
    private val controller: ConcatenationCanvasController by inject()
    private var outX = 0.0
    private var outY = 0.0
    lateinit var canvas: Canvas

    override val root: Parent = stackpane {
        println(controller.params)
        canvas(SettingsProvider.getCanvasWidth(), SettingsProvider.getCanvasHeight()) {
            vgrow = Priority.ALWAYS
            hgrow = Priority.ALWAYS
            canvas = this
            drawListener(model.drawingsProperty, graphicsContext2D)

            onScroll = ScalableScrollHandler(model, controller)

            onMouseDragged = MoveAxisHandler(model, controller)

            onMouseClicked = object : EventHandler<MouseEvent> {
                override fun handle(event: MouseEvent) {
                    val functions = model.drawings.filterIsInstance<Function>()
                    for (function in functions) {
                        for (point in function.pointArray) {
                            if (point.isNearBy(
                                    event.x + AbstractAxis.WIDTH_AXIS,
                                    SettingsProvider.getCanvasHeight() - event.y - AbstractAxis.WIDTH_AXIS
                                )
                            ) {
                                println("Show modal")
                            }
                        }
                    }
                }
            }

            setOnContextMenuRequested {
                withCoordContextmenu {
                    item("Add function").action {
                        val drawSize = DrawSize(
                            minX = 0.0,
                            maxX = this@canvas.width,
                            minY = 0.0,
                            maxY = this@canvas.height
                        )
                        val xDirections = model.drawings.filterIsInstance<Function>().map {
                            ExistDirection(it.xAxis.getDirection(), it.name)
                        }
                        val yDirections = model.drawings.filterIsInstance<Function>().map {
                            ExistDirection(it.yAxis.getDirection(), it.name)
                        }
                        controller.openFunctionModal(drawSize, xDirections, yDirections)
                    }
                    item("Add arrow").action {
                        controller.openArrowModal(outX, outY)
                    }
                    item("Add description").action {
                        controller.openDescriptionModal(outX, outY)
                    }
                }
            }
        }
    }

    private fun EventTarget.withCoordContextmenu(
        op: ContextMenu.() -> Unit = {}
    ): ContextMenu {
        val menu = (this as? Control)?.contextMenu ?: ContextMenu()
        op(menu)
        if (this is Control) {
            contextMenu = menu
        } else (this as? Node)?.apply {
            setOnContextMenuRequested { event ->
                outX = event.x
                outY = event.y
                menu.show(this, event.screenX, event.screenY)
                event.consume()
            }
        }
        return menu
    }
}