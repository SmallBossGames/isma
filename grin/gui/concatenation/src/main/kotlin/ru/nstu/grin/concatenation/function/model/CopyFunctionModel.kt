package ru.nstu.grin.concatenation.function.model

import javafx.beans.property.SimpleStringProperty
import tornadofx.ViewModel
import tornadofx.*

class CopyFunctionModel : ViewModel() {
    val function: ConcatenationFunction by param()

    var nameProperty = SimpleStringProperty()
    var name by nameProperty
}