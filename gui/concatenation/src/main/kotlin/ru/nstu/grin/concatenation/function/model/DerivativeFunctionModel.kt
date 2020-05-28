package ru.nstu.grin.concatenation.function.model

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.ViewModel
import tornadofx.*

class DerivativeFunctionModel : ViewModel() {
    var degreeProperty = SimpleIntegerProperty(this, "degreeProperty", 1)
    var degree by degreeProperty

    var derivativeTypeProperty = SimpleObjectProperty<DerivativeType>(DerivativeType.BOTH)
    var derivativeType by derivativeTypeProperty
}