package ru.nstu.grin.concatenation.function.model

import javafx.beans.property.SimpleStringProperty
import tornadofx.ViewModel
import tornadofx.*

class CopyFunctionModel : ViewModel() {
    var nameProperty = SimpleStringProperty()
    var name by nameProperty
}