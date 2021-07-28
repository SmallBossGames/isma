package ru.isma.next.app.viewmodels

import ru.isma.next.app.models.simulation.EventDetectionParametersModel
import tornadofx.booleanProperty
import tornadofx.doubleProperty
import tornadofx.getValue
import tornadofx.setValue

class EventDetectionParametersViewModel {
    val isEventDetectionInUseProperty = booleanProperty()
    var isEventDetectionInUse by isEventDetectionInUseProperty

    val isStepLimitInUseProperty = booleanProperty()
    var isStepLimitInUse by isStepLimitInUseProperty

    val gammaProperty = doubleProperty()
    var gamma by gammaProperty

    val lowBorderProperty = doubleProperty()
    var lowBorder by lowBorderProperty

    fun commit(model: EventDetectionParametersModel){
        isEventDetectionInUse = model.isEventDetectionInUse
        isStepLimitInUse = model.isStepLimitInUse
        gamma = model.gamma
        lowBorder = model.lowBorder
    }

    fun snapshot() = EventDetectionParametersModel(isEventDetectionInUse, isStepLimitInUse, gamma, lowBorder)
}