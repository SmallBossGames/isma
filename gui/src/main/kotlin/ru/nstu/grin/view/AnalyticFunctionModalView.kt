package ru.nstu.grin.view

import javafx.scene.Parent
import tornadofx.*

/**
 * @author Konstantin Volivach
 */
class AnalyticFunctionModalView : View() {
    override val root: Parent = vbox {
        label("Введите функцию в аналитическом виде:")
        textfield()
        button("OK") {
            action {
                TODO("Тут будет вызываться парс функции")
            }
        }
    }
}