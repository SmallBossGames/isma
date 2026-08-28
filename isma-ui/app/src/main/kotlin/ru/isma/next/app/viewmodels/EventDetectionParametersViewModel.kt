package ru.isma.next.app.viewmodels

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleDoubleProperty
import ru.isma.next.app.models.simulation.EventDetectionParametersModel

class EventDetectionParametersViewModel {
    val isEventDetectionInUseProperty = SimpleBooleanProperty(false)
    var isEventDetectionInUse: Boolean
        get() = isEventDetectionInUseProperty.value
        set(value) { isEventDetectionInUseProperty.value = value }

    val isStepLimitInUseProperty = SimpleBooleanProperty(false)
    var isStepLimitInUse: Boolean
        get() = isStepLimitInUseProperty.value
        set(value) { isStepLimitInUseProperty.value = value }

    val gammaProperty = SimpleDoubleProperty(0.0)
    var gamma: Double
        get() = gammaProperty.value
        set(value) { gammaProperty.value = value }
    fun gammaProperty() = gammaProperty

    val lowBorderProperty = SimpleDoubleProperty(0.0)
    var lowBorder: Double
        get() = lowBorderProperty.value
        set(value) { lowBorderProperty.value = value }
    fun lowBorderProperty() = lowBorderProperty

    fun commit(model: EventDetectionParametersModel){
        isEventDetectionInUse = model.isEventDetectionInUse
        isStepLimitInUse = model.isStepLimitInUse
        gamma = model.gamma
        lowBorder = model.lowBorder
    }

    fun snapshot() = EventDetectionParametersModel(isEventDetectionInUse, isStepLimitInUse, gamma, lowBorder)
}
