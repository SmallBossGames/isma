package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.Parent
import javafx.scene.control.TabPane
import tornadofx.Fragment
import tornadofx.tabpane
import tornadofx.*

class ElementsView : Fragment() {
    override val root: Parent = tabpane {
        tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
        tab("Функции") {
            add<FunctionListView>()
        }
        tab("Оси") {
            add<AxisListView>()
        }
        tab("Пространства") {
            add<CartesianListView>()
        }
    }
}