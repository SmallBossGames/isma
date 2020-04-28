package ru.nstu.grin.concatenation.function.model

import javafx.beans.property.SimpleObjectProperty
import ru.nstu.grin.common.model.ChooseFunctionWay
import ru.nstu.grin.common.model.DrawSize
import ru.nstu.grin.concatenation.canvas.model.ExistDirection
import tornadofx.ViewModel
import tornadofx.*

class ChooseFunctionViewModel : ViewModel() {
    val drawSize: DrawSize by param()
    val xExistDirections: List<ExistDirection> by param()
    val yExistDirections: List<ExistDirection> by param()

    val wayProperty = SimpleObjectProperty<ChooseFunctionWay>(ChooseFunctionWay.INPUT)
    val way: ChooseFunctionWay by wayProperty
}