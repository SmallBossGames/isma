package ru.isma.next.app.viewmodels

import ru.isma.next.app.models.simulation.EventDetectionParametersModel
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleDoubleProperty
import ru.isma.javafx.extensions.helpers.getValue
import ru.isma.javafx.extensions.helpers.setValue

class EventDetectionParametersViewModel {
    val isEventDetectionInUseProperty = SimpleBooleanProperty()
    var isEventDetectionInUse by isEventDetectionInUseProperty

    val isStepLimitInUseProperty = SimpleBooleanProperty()
    var isStepLimitInUse by isStepLimitInUseProperty

    val gammaProperty = SimpleDoubleProperty()
    var gamma by gammaProperty

    val lowBorderProperty = SimpleDoubleProperty()
    var lowBorder by lowBorderProperty

    fun commit(model: EventDetectionParametersModel){
        isEventDetectionInUse = model.isEventDetectionInUse
        isStepLimitInUse = model.isStepLimitInUse
        gamma = model.gamma
        lowBorder = model.lowBorder
    }

    fun snapshot() = EventDetectionParametersModel(isEventDetectionInUse, isStepLimitInUse, gamma, lowBorder)
}