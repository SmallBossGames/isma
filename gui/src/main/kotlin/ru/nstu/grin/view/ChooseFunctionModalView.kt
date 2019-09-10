package ru.nstu.grin.view

import javafx.scene.Parent
import tornadofx.View
import tornadofx.button
import tornadofx.vbox

class ChooseFunctionModalView : View() {
    override val root: Parent = vbox {
        button("Добавить функцию из файла") {

        }
        button("Добавить функцию вручную по точкма") {

        }

        button("Добавить функцию аналитически") {

        }
    }
}