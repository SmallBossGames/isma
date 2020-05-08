package ru.nstu.grin.concatenation.canvas.model

import javafx.beans.property.SimpleListProperty
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import tornadofx.ViewModel
import tornadofx.*

class FunctionListViewModel : ViewModel() {
    var functionsProperty = SimpleListProperty<ConcatenationFunction>()
    var functions by functionsProperty
}