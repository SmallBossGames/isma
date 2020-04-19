package ru.nstu.grin.simple.model.view

import javafx.beans.property.SimpleListProperty
import javafx.collections.FXCollections
import javafx.stage.Stage
import ru.nstu.grin.common.model.Arrow
import ru.nstu.grin.common.model.Description
import ru.nstu.grin.simple.model.PointToolTipSettings
import ru.nstu.grin.simple.model.SimpleFunction
import ru.nstu.grin.simple.view.SimpleCanvas
import ru.nstu.grin.simple.view.SimplePlotSettings
import tornadofx.ItemViewModel
import tornadofx.*

class SimpleCanvasViewModel : ItemViewModel<SimpleCanvas>() {
    var functionsProperty = SimpleListProperty<SimpleFunction>(FXCollections.observableArrayList())
    var functions by functionsProperty

    var arrowsProperty = SimpleListProperty<Arrow>(FXCollections.observableArrayList())
    var arrows by arrowsProperty

    var descriptionProperty = SimpleListProperty<Description>(FXCollections.observableArrayList())
    var descriptions by descriptionProperty

    val settings = SimplePlotSettings(120.0, 0.0, 0.0, 1.0)

    val pointToolTipSettings = PointToolTipSettings(false, 0.0, 0.0, 0.0, 0.0)
}