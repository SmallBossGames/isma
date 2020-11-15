package ru.nstu.grin.concatenation.function.model

import javafx.beans.property.SimpleListProperty
import javafx.collections.FXCollections
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import tornadofx.ViewModel
import tornadofx.*

class FunctionListViewModel : ViewModel() {
    var functionsProperty = SimpleListProperty<ConcatenationFunction>(FXCollections.observableArrayList())
    var functions by functionsProperty
}