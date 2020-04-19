package ru.nstu.grin.simple.view

import javafx.scene.Parent
import ru.nstu.grin.simple.events.TurnLogarithmScaleEvent
import ru.nstu.grin.simple.model.LogarithmAxis
import ru.nstu.grin.simple.view.modal.ChooseFunctionModalView
import tornadofx.View
import tornadofx.action
import tornadofx.item
import tornadofx.menu
import tornadofx.menubar
import tornadofx.vbox

class SimpleCanvasView : View() {
    override val root: Parent = vbox {
        menubar {
            menu("File") {
                item("Save as").action {
                }
                item("Load").action {
                }
            }
            menu("Опции") {
                item("Добавить функцию").action {
                    find<ChooseFunctionModalView>().openModal()
                }
                menu("Логарифмический масштаб") {
                    item("Включить по x").action {
                        val event = TurnLogarithmScaleEvent(
                            axis = LogarithmAxis.X
                        )
                        fire(event)
                    }
                    item("Включить по y").action {
                        val event = TurnLogarithmScaleEvent(
                            axis = LogarithmAxis.Y
                        )
                        fire(event)
                    }
                }
                item("Clear all").action {
                }
            }
        }
        add<SimpleCanvas>()
    }
}