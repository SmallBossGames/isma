package ru.nstu.grin.concatenation.function.model

import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.ViewModel
import tornadofx.*

class FunctionIntegrationFragmentModel : ViewModel() {
    var integrationMethodProperty = SimpleObjectProperty(IntegrationMethod.TRAPEZE)
    var integrationMethod by integrationMethodProperty


    var leftBorderProperty = SimpleDoubleProperty(this, "leftBorderProperty", 0.0)
    var leftBorder by leftBorderProperty

    var rightBorderProperty = SimpleDoubleProperty(this, "rightBorderProperty", 0.0)
    var rightBorder by rightBorderProperty
}