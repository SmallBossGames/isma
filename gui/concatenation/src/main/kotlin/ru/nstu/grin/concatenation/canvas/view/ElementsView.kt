package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.Parent
import tornadofx.Fragment
import tornadofx.tabpane
import tornadofx.*

class ElementsView : Fragment() {
    override val root: Parent = tabpane {
        tab("Функции") {
            add<FunctionListView>()
        }
        tab("Оси") {
            add<AxisListView>()
        }
    }
}