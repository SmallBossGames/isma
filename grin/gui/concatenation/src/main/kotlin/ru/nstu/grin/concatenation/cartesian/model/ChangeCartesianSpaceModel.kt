package ru.nstu.grin.concatenation.cartesian.model

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import ru.isma.javafx.extensions.helpers.setValue
import ru.isma.javafx.extensions.helpers.getValue

class ChangeCartesianSpaceModel(
    val space: CartesianSpace
) {

    val nameProperty = SimpleStringProperty("")
    var name:String by nameProperty

    val isShowGridProperty = SimpleBooleanProperty()
    var isShowGrid by isShowGridProperty

    init {
        name = space.name
        isShowGrid = space.isShowGrid
    }
}