package ru.nstu.grin.model.view

import javafx.beans.property.SimpleListProperty
import javafx.collections.FXCollections
import ru.nstu.grin.model.Drawable
import ru.nstu.grin.view.ConcatenationCanvas
import tornadofx.ItemViewModel
import tornadofx.*

class ConcatenationCanvasModelViewModel : ItemViewModel<ConcatenationCanvas>() {
    var drawingsProperty = SimpleListProperty<Drawable>(FXCollections.observableArrayList())
    var drawings by drawingsProperty
}