package ru.isma.next.app.models

import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.booleanProperty
import tornadofx.getValue
import tornadofx.setValue

class ResultProcessingParametersModel {
    val isSimplifyInUseProperty = booleanProperty()
    var isSimplifyInUse by isSimplifyInUseProperty

    val selectedSimplifyMethodProperty = SimpleStringProperty()
    var selectedSimplifyMethod: String by selectedSimplifyMethodProperty

    val toleranceProperty = SimpleDoubleProperty()
    var tolerance by toleranceProperty
}