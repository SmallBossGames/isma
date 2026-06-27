package ru.isma.next.app.viewmodels

import javafx.beans.property.*
import ru.isma.next.app.models.simulation.IntegrationMethodParametersModel

class IntegrationMethodParametersViewModel {
    val selectedMethodProperty = SimpleStringProperty("")
    var selectedMethod: String
        get() = selectedMethodProperty.value
        set(value) { selectedMethodProperty.value = value }
    fun selectedMethodProperty() = selectedMethodProperty

    val accuracyProperty = SimpleDoubleProperty(0.0)
    var accuracy: Double
        get() = accuracyProperty.value
        set(value) { accuracyProperty.value = value }
    fun accuracyProperty() = accuracyProperty

    val isAccuracyInUseProperty = SimpleBooleanProperty(false)
    var isAccuracyInUse: Boolean
        get() = isAccuracyInUseProperty.value
        set(value) { isAccuracyInUseProperty.value = value }

    val isStableAllowedInUseProperty = SimpleBooleanProperty(false)
    var isStableAllowedInUse: Boolean
        get() = isStableAllowedInUseProperty.value
        set(value) { isStableAllowedInUseProperty.value = value }

    val isStableInUseProperty = SimpleBooleanProperty(false)
    var isStableInUse: Boolean
        get() = isStableInUseProperty.value
        set(value) { isStableInUseProperty.value = value }

    val isParallelInUseProperty = SimpleBooleanProperty(false)
    var isParallelInUse: Boolean
        get() = isParallelInUseProperty.value
        set(value) { isParallelInUseProperty.value = value }

    val serverProperty = SimpleStringProperty("")
    var server: String
        get() = serverProperty.value
        set(value) { serverProperty.value = value }
    fun serverProperty() = serverProperty

    val portProperty = SimpleIntegerProperty(0)
    var port: Int
        get() = portProperty.value
        set(value) { portProperty.value = value }
    fun portProperty() = portProperty

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
