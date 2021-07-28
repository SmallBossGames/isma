package ru.isma.next.app.viewmodels

import tornadofx.*
import javafx.beans.property.SimpleDoubleProperty
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import ru.isma.next.app.models.simulation.CauchyInitialsModel

class CauchyInitialsViewModel {
    val startTimeProperty = SimpleDoubleProperty()
    var startTime by startTimeProperty

    val endTimeProperty = SimpleDoubleProperty()
    var endTime by endTimeProperty

    val stepProperty = SimpleDoubleProperty()
    var step by stepProperty

    fun commit(model: CauchyInitialsModel){
        startTime = model.startTime
        endTime = model.endTime
        step = model.initialStep
    }

    fun snapshot() = CauchyInitialsModel(startTime, endTime, step)
}