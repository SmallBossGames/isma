package ru.nstu.grin.model.view

import javafx.beans.property.SimpleListProperty
import javafx.collections.FXCollections
import ru.nstu.grin.model.drawable.Arrow
import ru.nstu.grin.model.drawable.Description
import ru.nstu.grin.model.drawable.SimpleFunction
import ru.nstu.grin.view.simple.SimpleCanvas
import tornadofx.ItemViewModel
import tornadofx.*

class SimpleCanvasViewModel : ItemViewModel<SimpleCanvas>() {
    var functionsProperty = SimpleListProperty<SimpleFunction>(FXCollections.observableArrayList())
    var functions by functionsProperty

    var arrowsProperty = SimpleListProperty<Arrow>(FXCollections.observableArrayList())
    var arrows by arrowsProperty

    var descriptionProperty = SimpleListProperty<Description>(FXCollections.observableArrayList())
    var descriptions by descriptionProperty
}