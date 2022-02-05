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
        tab("Functions") {
            add<FunctionListView>()
        }
        tab("Axes") {
            add<AxisListView>()
        }
        tab("Spaces") {
            add<CartesianListView>()
        }
        tab("Descriptions") {
            add<DescriptionListView>()
        }
    }
}