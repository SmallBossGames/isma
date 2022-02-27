package ru.nstu.grin.concatenation.cartesian.model

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.ViewModel
import tornadofx.*

class ChangeCartesianSpaceModel : ViewModel() {
    val space: CartesianSpace by param()

    val nameProperty = SimpleStringProperty()
    var name by nameProperty

    val isShowGridProperty = SimpleBooleanProperty()
    var isShowGrid by isShowGridProperty

    init {
        name = space.name
        isShowGrid = space.isShowGrid
    }
}