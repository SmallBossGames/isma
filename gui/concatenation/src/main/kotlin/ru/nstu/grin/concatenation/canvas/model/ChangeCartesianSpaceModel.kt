package ru.nstu.grin.concatenation.canvas.model

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.ViewModel
import tornadofx.*

class ChangeCartesianSpaceModel : ViewModel() {
    var nameProperty = SimpleStringProperty()
    var name by nameProperty

    var isShowGridProperty = SimpleBooleanProperty()
    var isShowGrid by isShowGridProperty
}