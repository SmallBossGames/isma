package ru.nstu.grin.view

import javafx.scene.Parent
import tornadofx.*

/**
 * @author Konstantin Volivach
 */
class ManualEnterFunctionView : View() {
    override val root: Parent = vbox {
        hbox {
            label("Введите ниже точки:")
            button("Формат") {
                action {
                    TODO("Здесь должна быть еще одна модалка с форматом, или переключение лэйаута модалки")
                }
            }
        }
        textarea {
        }
        button("OK") {
            action {
                TODO("Action to create function")
            }
        }
    }
}