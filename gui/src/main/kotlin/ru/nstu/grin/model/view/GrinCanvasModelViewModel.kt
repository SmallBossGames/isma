package ru.nstu.grin.model.view

import javafx.beans.property.SimpleListProperty
import javafx.collections.FXCollections
import ru.nstu.grin.model.Edge
import ru.nstu.grin.model.drawable.Arrow
import ru.nstu.grin.model.drawable.Description
import ru.nstu.grin.model.drawable.Function
import ru.nstu.grin.view.GrinCanvas
import tornadofx.ItemViewModel
import tornadofx.*

class GrinCanvasModelViewModel : ItemViewModel<GrinCanvas>() {
    val functionsProperty = SimpleListProperty<Function>(FXCollections.observableArrayList())
    var functions by functionsProperty

    val descriptionsProperty = SimpleListProperty<Description>(FXCollections.observableArrayList())
    var descriptions by descriptionsProperty

    val arrowsProperty = SimpleListProperty<Arrow>(FXCollections.observableArrayList())
    var arrows by arrowsProperty

    val edgesProperty = SimpleListProperty<Edge>(FXCollections.observableArrayList())
    val edges by edgesProperty
}