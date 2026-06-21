package ru.isma.next.app.viewmodels

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleDoubleProperty
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.isma.javafx.extensions.viewmodel.BaseViewModel
import ru.isma.javafx.extensions.helpers.getValue
import ru.isma.javafx.extensions.helpers.setValue
import ru.isma.next.app.models.simulation.EventDetectionParametersModel

class EventDetectionParametersViewModel : BaseViewModel() {
    private val _isEventDetectionInUseFlow = MutableStateFlow(false)
    val isEventDetectionInUseFlow: StateFlow<Boolean> = _isEventDetectionInUseFlow.asStateFlow()

    private val _isStepLimitInUseFlow = MutableStateFlow(false)
    val isStepLimitInUseFlow: StateFlow<Boolean> = _isStepLimitInUseFlow.asStateFlow()

    private val _gammaFlow = MutableStateFlow(0.0)
    val gammaFlow: StateFlow<Double> = _gammaFlow.asStateFlow()

    private val _lowBorderFlow = MutableStateFlow(0.0)
    val lowBorderFlow: StateFlow<Double> = _lowBorderFlow.asStateFlow()

    val isEventDetectionInUseProperty = SimpleBooleanProperty(false).also {
        it.addListener { _, _, newValue -> _isEventDetectionInUseFlow.value = newValue as Boolean }
    }
    var isEventDetectionInUse by isEventDetectionInUseProperty

    val isStepLimitInUseProperty = SimpleBooleanProperty(false).also {
        it.addListener { _, _, newValue -> _isStepLimitInUseFlow.value = newValue as Boolean }
    }
    var isStepLimitInUse by isStepLimitInUseProperty

    val gammaProperty = SimpleDoubleProperty(0.0).also {
        it.addListener { _, _, newValue -> _gammaFlow.value = newValue as Double }
    }
    var gamma by gammaProperty

    val lowBorderProperty = SimpleDoubleProperty(0.0).also {
        it.addListener { _, _, newValue -> _lowBorderFlow.value = newValue as Double }
    }
    var lowBorder by lowBorderProperty

    fun isEventDetectionInUseProperty() = isEventDetectionInUseProperty
    fun isStepLimitInUseProperty() = isStepLimitInUseProperty
    fun gammaProperty() = gammaProperty
    fun lowBorderProperty() = lowBorderProperty

    fun commit(model: EventDetectionParametersModel){
        isEventDetectionInUse = model.isEventDetectionInUse
        isStepLimitInUse = model.isStepLimitInUse
        gamma = model.gamma
        lowBorder = model.lowBorder
    }

    fun snapshot() = EventDetectionParametersModel(isEventDetectionInUse, isStepLimitInUse, gamma, lowBorder)
}
