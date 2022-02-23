package ru.nstu.grin.concatenation.function.model

import javafx.collections.FXCollections
import tornadofx.ViewModel

class IntersectionFunctionModel : ViewModel() {
    val functions = FXCollections.observableArrayList<ConcatenationFunction>()!!
}