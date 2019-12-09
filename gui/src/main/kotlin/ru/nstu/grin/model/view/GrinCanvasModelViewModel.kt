package ru.nstu.grin.model.view

import javafx.beans.property.SimpleListProperty
import javafx.collections.FXCollections
import ru.nstu.grin.model.Drawable
import ru.nstu.grin.model.Edge
import ru.nstu.grin.model.drawable.Arrow
import ru.nstu.grin.model.drawable.Description
import ru.nstu.grin.model.drawable.Function
import ru.nstu.grin.view.GrinCanvas
import tornadofx.ItemViewModel
import tornadofx.*

class GrinCanvasModelViewModel : ItemViewModel<GrinCanvas>() {
    var drawingsProperty = SimpleListProperty<Drawable>(FXCollections.observableArrayList())
    var drawings by drawingsProperty
}