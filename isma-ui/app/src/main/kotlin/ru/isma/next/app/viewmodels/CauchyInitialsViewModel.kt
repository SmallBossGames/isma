package ru.isma.next.app.viewmodels

import javafx.beans.property.SimpleDoubleProperty
import ru.isma.next.app.models.simulation.CauchyInitialsModel

class CauchyInitialsViewModel {
    val startTimeProperty = SimpleDoubleProperty(0.0)
    var startTime: Double
        get() = startTimeProperty.value
        set(value) { startTimeProperty.value = value }
    fun startTimeProperty() = startTimeProperty

    val endTimeProperty = SimpleDoubleProperty(0.0)
    var endTime: Double
        get() = endTimeProperty.value
        set(value) { endTimeProperty.value = value }
    fun endTimeProperty() = endTimeProperty

    val stepProperty = SimpleDoubleProperty(0.1)
    var step: Double
        get() = stepProperty.value
        set(value) { stepProperty.value = value }
    fun stepProperty() = stepProperty

    fun commit(model: CauchyInitialsModel){
        startTime = model.startTime
        endTime = model.endTime
        step = model.initialStep
    }

    fun snapshot() = CauchyInitialsModel(startTime, endTime, step)
}
