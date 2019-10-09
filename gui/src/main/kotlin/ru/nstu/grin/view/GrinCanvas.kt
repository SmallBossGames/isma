package ru.nstu.grin.view

import javafx.scene.Parent
import javafx.stage.StageStyle
import ru.nstu.grin.controller.GrinCanvasController
import ru.nstu.grin.model.Context
import ru.nstu.grin.model.GrinCanvasModel
import tornadofx.*

class GrinCanvas : View() {
    companion object {
        private const val WIDTH = 1200.0
        private const val HEIGHT = 800.0
    }

    private val context = Context(
        WIDTH,
        HEIGHT
    )

    private val grinCanvasModel: GrinCanvasModel by inject()

    private val grinCanvasController: GrinCanvasController by inject()

    init {

    }

    override val root: Parent = stackpane {
        val test = grinCanvasController.params
        group {
            add(context.canvas)
        }

        context.canvas.setOnContextMenuRequested { contextEvent ->
            contextmenu {
                menuitem("Add function").action {
                    find<ChooseFunctionModalView>().openModal()
                }
                menuitem("Add arrow").action {
                    find<ArrowModalView>(
                        mapOf(
                            ArrowModalView::x to contextEvent.screenX,
                            ArrowModalView::y to contextEvent.screenY
                        )
                    ).openModal(stageStyle = StageStyle.UTILITY)
                }
            }
        }
    }
}