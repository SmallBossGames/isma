package ru.nstu.grin.model

import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class ManualEnterFunctionModel : ViewModel() {
    var textProperty = SimpleStringProperty()
    var text: String by textProperty
}