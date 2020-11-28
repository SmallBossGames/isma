package ru.nstu.grin.simple.model

import javafx.beans.property.SimpleObjectProperty
import ru.nstu.grin.common.model.ChooseFunctionWay
import tornadofx.ViewModel
import tornadofx.getValue

class ChooseFunctionViewModel : ViewModel() {
    val wayProperty = SimpleObjectProperty<ChooseFunctionWay>(ChooseFunctionWay.INPUT)
    val way: ChooseFunctionWay by wayProperty
}