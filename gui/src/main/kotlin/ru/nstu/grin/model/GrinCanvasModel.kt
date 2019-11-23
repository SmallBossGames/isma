package ru.nstu.grin.model

import javafx.beans.property.SimpleListProperty
import javafx.collections.FXCollections
import javafx.scene.paint.Color
import ru.nstu.grin.Direction
import ru.nstu.grin.view.GrinCanvas
import tornadofx.ItemViewModel
import tornadofx.*

class GrinCanvasModel : ItemViewModel<GrinCanvas>() {
    val functionsProperty = SimpleListProperty<Function>(FXCollections.observableArrayList())
    var functions by functionsProperty

    val descriptionsProperty = SimpleListProperty<Description>(FXCollections.observableArrayList())
    var descriptions by descriptionsProperty

    val arrowsProperty = SimpleListProperty<Arrow>(FXCollections.observableArrayList())
    var arrows by arrowsProperty

    val edgesProperty = SimpleListProperty<Edge>(FXCollections.observableArrayList())
    val edges by edgesProperty
}