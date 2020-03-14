package ru.nstu.grin.concatenation.view

import javafx.collections.ListChangeListener
import javafx.event.EventHandler
import javafx.event.EventTarget
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.canvas.Canvas
import javafx.scene.control.ContextMenu
import javafx.scene.control.Control
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Priority
import ru.nstu.grin.common.common.SettingsProvider
import ru.nstu.grin.concatenation.controller.ConcatenationCanvasController
import ru.nstu.grin.common.model.Arrow
import ru.nstu.grin.concatenation.model.CartesianSpace
import ru.nstu.grin.common.model.Description
import ru.nstu.grin.common.model.DrawSize
import ru.nstu.grin.concatenation.model.view.ConcatenationCanvasModelViewModel
import tornadofx.*

class ConcatenationCanvas : View() {
    private val model: ConcatenationCanvasModelViewModel by inject()
    private val controller: ConcatenationCanvasController by inject()
    private var outX = 0.0
    private var outY = 0.0
    lateinit var canvas: Canvas
    private lateinit var chainDrawer: ConcatenationChainDrawer

    override val root: Parent = stackpane {
        println(controller.params)
        canvas(SettingsProvider.getCanvasWidth(), SettingsProvider.getCanvasHeight()) {
            vgrow = Priority.ALWAYS
            hgrow = Priority.ALWAYS
            canvas = this
            chainDrawer = ConcatenationChainDrawer(this, model)

            model.arrowsProperty.addListener { _: ListChangeListener.Change<out Arrow> -> chainDrawer.draw() }
            model.cartesianSpace.addListener { _: ListChangeListener.Change<out CartesianSpace> -> chainDrawer.draw() }
            model.descriptionsProperty.addListener { _: ListChangeListener.Change<out Description> -> chainDrawer.draw() }

            onScroll = ScalableScrollHandler(model, controller)

            onMouseDragged = MoveAxisHandler(model, controller)

            onMouseClicked = object : EventHandler<MouseEvent> {
                override fun handle(event: MouseEvent) {
                    println("Show modal with coordinates")
//                    val functions = model.drawings.filterIsInstance<ConcatenationFunction>()
//                    for (function in functions) {
//                        for (point in function.points) {
//                            if (point.isNearBy(
//                                    event.x + AbstractAxis.WIDTH_AXIS,
//                                    SettingsProvider.getCanvasHeight() - event.y - AbstractAxis.WIDTH_AXIS
//                                )
//                            ) {
//                                println("Show modal")
//                            }
//                        }
//                    }
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
//                        val xDirections = model.drawings.filterIsInstance<ConcatenationFunction>().map {
//                            ExistDirection(it.xAxis.getDirection(), it.name)
//                        }
//                        val yDirections = model.drawings.filterIsInstance<ConcatenationFunction>().map {
//                            ExistDirection(it.yAxis.getDirection(), it.name)
//                        }
                        // TODO add xDirection, yDirection
                        controller.openFunctionModal(drawSize, listOf(), listOf())
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