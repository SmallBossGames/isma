package ru.isma.next.app.viewmodels

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import ru.isma.next.services.simulation.abstractions.models.IntegrationMethodParametersModel
import tornadofx.getValue
import tornadofx.setValue

class IntegrationMethodParametersViewModel {
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

    fun commit(model: IntegrationMethodParametersModel){
        selectedMethod = model.selectedMethod
        accuracy = model.accuracy
        isAccuracyInUse = model.isAccuracyInUse
        isStableAllowedInUse = model.isStableAllowedInUse
        isStableInUse = model.isStableInUse
        isParallelInUse = model.isParallelInUse
        server = model.server
        port = model.port
    }

    fun snapshot() = IntegrationMethodParametersModel(selectedMethod, accuracy, isAccuracyInUse, isStableAllowedInUse, isStableInUse, isParallelInUse, server, port)
}