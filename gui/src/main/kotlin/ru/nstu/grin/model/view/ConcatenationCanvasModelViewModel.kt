package ru.nstu.grin.model.view

import javafx.beans.property.SimpleListProperty
import javafx.collections.FXCollections
import ru.nstu.grin.model.drawable.Arrow
import ru.nstu.grin.model.drawable.CartesianSpace
import ru.nstu.grin.model.drawable.Description
import ru.nstu.grin.view.concatenation.ConcatenationCanvas
import tornadofx.ItemViewModel
import tornadofx.*

class ConcatenationCanvasModelViewModel : ItemViewModel<ConcatenationCanvas>() {
    var cartesianSpaceProperty = SimpleListProperty<CartesianSpace>(FXCollections.observableArrayList())
    var cartesianSpace by cartesianSpaceProperty

    var arrowsProperty = SimpleListProperty<Arrow>(FXCollections.observableArrayList())
    var arrows by arrowsProperty

    var descriptionsProperty = SimpleListProperty<Description>(FXCollections.observableArrayList())
    var descriptions by descriptionsProperty
}