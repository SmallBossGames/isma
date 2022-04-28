package ru.isma.next.app.viewmodels

import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.booleanProperty
import ru.isma.javafx.extensions.helpers.getValue
import ru.isma.javafx.extensions.helpers.setValue

class ResultProcessingParametersViewModel {
    val isSimplifyInUseProperty = booleanProperty()
    var isSimplifyInUse by isSimplifyInUseProperty

    val selectedSimplifyMethodProperty = SimpleStringProperty()
    var selectedSimplifyMethod: String by selectedSimplifyMethodProperty

    val toleranceProperty = SimpleDoubleProperty()
    var tolerance by toleranceProperty
}