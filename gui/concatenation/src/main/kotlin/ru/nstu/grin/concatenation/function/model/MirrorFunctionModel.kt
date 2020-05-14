package ru.nstu.grin.concatenation.function.model

import javafx.beans.property.SimpleListProperty
import javafx.collections.FXCollections
import tornadofx.ViewModel
import tornadofx.*

class MirrorFunctionModel : ViewModel() {
    var functionsProperty = SimpleListProperty<ConcatenationFunction>(FXCollections.observableArrayList())
    var functions by functionsProperty
}