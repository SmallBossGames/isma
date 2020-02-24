package ru.nstu.grin.model.simple

import javafx.beans.property.SimpleObjectProperty
import ru.nstu.grin.model.ChooseFunctionWay
import tornadofx.ViewModel
import tornadofx.getValue

class ChooseFunctionViewModel : ViewModel() {
    val wayProperty = SimpleObjectProperty<ChooseFunctionWay>(ChooseFunctionWay.INPUT)
    val way: ChooseFunctionWay by wayProperty
}