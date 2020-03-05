package ru.nstu.grin.model.view

import javafx.beans.property.SimpleListProperty
import javafx.collections.FXCollections
import ru.nstu.grin.model.Drawable
import ru.nstu.grin.model.drawable.Arrow
import ru.nstu.grin.model.drawable.ConcatenationFunction
import ru.nstu.grin.model.drawable.Description
import ru.nstu.grin.view.concatenation.ConcatenationCanvas
import tornadofx.ItemViewModel
import tornadofx.*

class ConcatenationCanvasModelViewModel : ItemViewModel<ConcatenationCanvas>() {
    var drawingsProperty = SimpleListProperty<Drawable>(FXCollections.observableArrayList())
    var drawings by drawingsProperty

    var functionsProperty = SimpleListProperty<ConcatenationFunction>(FXCollections.observableArrayList())
    var functions by functionsProperty

    var arrowsProperty = SimpleListProperty<Arrow>(FXCollections.observableArrayList())
    var arrows by arrowsProperty

    var descriptionsProperty = SimpleListProperty<Description>(FXCollections.observableArrayList())
    var descriptions by descriptionsProperty
}