package ru.nstu.grin.view

import javafx.scene.Parent
import javafx.stage.StageStyle
import ru.nstu.grin.controller.GrinCanvasController
import ru.nstu.grin.model.Context
import ru.nstu.grin.model.GrinCanvasModel
import tornadofx.*

class GrinCanvas : View() {
    private val context = Context(
        WIDTH,
        HEIGHT
    )

    private val model: GrinCanvasModel by inject()

    private val controller: GrinCanvasController by inject()

    override val root: Parent = stackpane {
        val test = controller.params
//        context.canvas.bindChildren(model.functionsProperty) {
//            line {
//                startX = 0.0
//                startY = 10.0
//                endX = 20.0
//                endY = 20.0
//            }
//        }
        group {
            bindChildren(model.functionsProperty) {
                line {
                    startX = 0.0
                    startY = 10.0
                    endX = 20.0
                    endY = 20.0
                }
            }
            add(context.canvas)
            context
        }

        context.canvas.setOnContextMenuRequested { contextEvent ->
            contextmenu {
                item("Add function").action {
                    find<ChooseFunctionModalView>().openModal()
                }
                item("Add arrow").action {
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

    private companion object {
        private const val WIDTH = 1200.0
        private const val HEIGHT = 800.0
    }
}