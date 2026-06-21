package ru.isma.next.app.viewmodels

import javafx.beans.property.SimpleDoubleProperty
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.isma.javafx.extensions.viewmodel.BaseViewModel
import ru.isma.javafx.extensions.helpers.getValue
import ru.isma.javafx.extensions.helpers.setValue
import ru.isma.next.app.models.simulation.CauchyInitialsModel

class CauchyInitialsViewModel : BaseViewModel() {
    private val _startTimeFlow = MutableStateFlow(0.0)
    val startTimeFlow: StateFlow<Double> = _startTimeFlow.asStateFlow()

    private val _endTimeFlow = MutableStateFlow(0.0)
    val endTimeFlow: StateFlow<Double> = _endTimeFlow.asStateFlow()

    private val _stepFlow = MutableStateFlow(0.1)
    val stepFlow: StateFlow<Double> = _stepFlow.asStateFlow()

    private val startTimeProperty = SimpleDoubleProperty(0.0).also {
        it.addListener { _, _, newValue -> _startTimeFlow.value = newValue as Double }
    }
    private val endTimeProperty = SimpleDoubleProperty(0.0).also {
        it.addListener { _, _, newValue -> _endTimeFlow.value = newValue as Double }
    }
    private val stepProperty = SimpleDoubleProperty(0.1).also {
        it.addListener { _, _, newValue -> _stepFlow.value = newValue as Double }
    }

    var startTime by startTimeProperty
    var endTime by endTimeProperty
    var step by stepProperty

    fun startTimeProperty() = startTimeProperty
    fun endTimeProperty() = endTimeProperty
    fun stepProperty() = stepProperty

    fun commit(model: CauchyInitialsModel){
        startTime = model.startTime
        endTime = model.endTime
        step = model.initialStep
    }

    fun snapshot() = CauchyInitialsModel(startTime, endTime, step)
}
