package ru.nstu.grin.view

import javafx.collections.ListChangeListener
import javafx.event.EventTarget
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.control.ContextMenu
import javafx.scene.control.Control
import javafx.stage.StageStyle
import ru.nstu.grin.controller.GrinCanvasController
import ru.nstu.grin.extensions.function
import ru.nstu.grin.model.Arrow
import ru.nstu.grin.model.GrinCanvasModel
import tornadofx.*

class GrinCanvas : View() {
    private val model: GrinCanvasModel by inject()
    private val controller: GrinCanvasController by inject()
    private var outX = 0.0
    private var outY = 0.0

    override val root: Parent = stackpane {
        println(controller.params)
        flowpane {

            bindChildren(model.functionsProperty) {
                function(it)
            }
            canvas(WIDTH, HEIGHT) {
                model.arrowsProperty.addListener { llisner: ListChangeListener.Change<out Arrow> ->
                    llisner.list.forEach {
                        it.draw(graphicsContext2D)
                    }
                }
                setOnContextMenuRequested {
                    withCoordContextmenu {
                        item("Add function").action {
                            find<ChooseFunctionModalView>().openModal()
                        }
                        item("Add arrow").action {
                            find<ArrowModalView>(
                                mapOf(
                                    ArrowModalView::x to outX,
                                    ArrowModalView::y to outY
                                )
                            ).openModal(stageStyle = StageStyle.UTILITY)
                        }
                    }
                    it.consume()
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

