package ru.nstu.grin.concatenation.axis.model

import javafx.beans.property.SimpleListProperty
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import tornadofx.ViewModel
import tornadofx.*

class AxisListViewModel: ViewModel() {
    var axisesProperty = SimpleListProperty<ConcatenationAxis>()
    var axises by axisesProperty
}