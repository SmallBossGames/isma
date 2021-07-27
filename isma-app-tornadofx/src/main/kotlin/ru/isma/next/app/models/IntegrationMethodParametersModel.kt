package ru.isma.next.app.models

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.getValue
import tornadofx.onChange
import tornadofx.setValue

class IntegrationMethodParametersModel {
    val selectedMethodProperty = SimpleStringProperty()
    var selectedMethod: String by selectedMethodProperty

    val accuracyProperty = SimpleDoubleProperty()
    var accuracy by accuracyProperty

    val isAccuracyInUseProperty = SimpleBooleanProperty()
    var isAccuracyInUse by isAccuracyInUseProperty

    val isStableAllowedProperty = SimpleBooleanProperty()
    var isStableAllowedInUse by isStableAllowedProperty

    val isStableInUseProperty = SimpleBooleanProperty()
    var isStableInUse by isStableInUseProperty

    val isParallelInUseProperty = SimpleBooleanProperty()
    var isParallelInUse by isParallelInUseProperty

    val serverProperty = SimpleStringProperty()
    var server: String by serverProperty

    val portProperty = SimpleIntegerProperty()
    var port by portProperty

    init {
        selectedMethodProperty.onChange {

        }
    }
}