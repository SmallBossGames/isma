package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import ru.nstu.grin.concatenation.axis.view.AxisListView
import ru.nstu.grin.concatenation.cartesian.view.CartesianListView
import ru.nstu.grin.concatenation.description.view.DescriptionListView
import ru.nstu.grin.concatenation.function.view.FunctionListView

class ElementsView(
    functionListView: FunctionListView,
    axisListView: AxisListView,
    cartesianListView: CartesianListView,
    descriptionListView: DescriptionListView
) : TabPane(
    Tab("Functions", functionListView),
    Tab("Axes", axisListView),
    Tab("Spaces", cartesianListView),
    Tab("Descriptions", descriptionListView)
){
    init {
        tabClosingPolicy = TabClosingPolicy.UNAVAILABLE
    }
}