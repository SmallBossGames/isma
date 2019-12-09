package ru.nstu.grin.view

import javafx.event.EventHandler
import javafx.event.EventTarget
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.canvas.Canvas
import javafx.scene.control.ContextMenu
import javafx.scene.control.Control
import javafx.scene.input.ScrollEvent
import ru.nstu.grin.controller.ClearCanvasEvent
import ru.nstu.grin.controller.GrinCanvasController
import ru.nstu.grin.extensions.drawListener
import ru.nstu.grin.model.CoordinateDirection
import ru.nstu.grin.model.Direction
import ru.nstu.grin.model.DrawSize
import ru.nstu.grin.model.drawable.Function
import ru.nstu.grin.model.view.GrinCanvasModelViewModel
import tornadofx.*

class GrinCanvas : View() {
    private val model: GrinCanvasModelViewModel by inject()
    private val controller: GrinCanvasController by inject()
    private var outX = 0.0
    private var outY = 0.0
    lateinit var canvas: Canvas

    override val root: Parent = stackpane {
        println(controller.params)
        flowpane {
            canvas(WIDTH, HEIGHT) {
                canvas = this
                drawListener(model.drawingsProperty, graphicsContext2D)

                onScroll = ScalableScrollHandler(model, controller)

                setOnContextMenuRequested {
                    withCoordContextmenu {
                        item("Add function").action {
                            val drawSize = DrawSize(
                                minX = 0.0,
                                maxX = this@canvas.width,
                                minY = 0.0,
                                maxY = this@canvas.height
                            )
                            controller.openFunctionModal(drawSize)
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

    private companion object {
        private const val WIDTH = 1200.0
        private const val HEIGHT = 800.0
    }
}

