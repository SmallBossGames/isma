package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.Parent
import javafx.scene.control.TabPane
import ru.nstu.grin.concatenation.axis.view.AxisListView
import ru.nstu.grin.concatenation.cartesian.view.CartesianListView
import ru.nstu.grin.concatenation.description.view.DescriptionListView
import ru.nstu.grin.concatenation.function.view.FunctionListView
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
        tab("Описания") {
            add<DescriptionListView>()
        }
    }
}