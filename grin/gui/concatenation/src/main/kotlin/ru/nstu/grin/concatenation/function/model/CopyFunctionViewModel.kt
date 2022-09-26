package ru.nstu.grin.concatenation.function.model

import javafx.beans.property.SimpleStringProperty
import ru.isma.javafx.extensions.helpers.getValue
import ru.isma.javafx.extensions.helpers.setValue
import ru.nstu.grin.concatenation.function.service.FunctionCanvasService

class CopyFunctionViewModel(
    private val function: ConcatenationFunction,
    private val functionCanvasService: FunctionCanvasService
) {
    val nameProperty = SimpleStringProperty("")
    var name: String by nameProperty

    fun commit() {
        functionCanvasService.copyFunction(function, name)
    }
}