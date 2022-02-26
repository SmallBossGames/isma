package ru.nstu.grin.concatenation.function.model

import javafx.collections.FXCollections

class FunctionListViewModel {
    val functions = FXCollections.observableArrayList<ConcatenationFunction>()!!
}