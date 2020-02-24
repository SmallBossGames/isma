package ru.nstu.grin.model.concatenation

import javafx.beans.property.SimpleObjectProperty
import ru.nstu.grin.model.ChooseFunctionWay
import ru.nstu.grin.model.DrawSize
import ru.nstu.grin.model.ExistDirection
import tornadofx.ViewModel
import tornadofx.getValue

class ChooseFunctionViewModel : ViewModel() {
    val drawSize: DrawSize by param()
    val xExistDirections: List<ExistDirection> by param()
    val yExistDirections: List<ExistDirection> by param()

    val wayProperty = SimpleObjectProperty<ChooseFunctionWay>(ChooseFunctionWay.INPUT)
    val way: ChooseFunctionWay by wayProperty
}