package ru.isma.next.app.viewmodels

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.isma.javafx.extensions.viewmodel.BaseViewModel
import ru.isma.javafx.extensions.helpers.getValue
import ru.isma.javafx.extensions.helpers.setValue
import ru.isma.next.app.models.simulation.IntegrationMethodParametersModel

class IntegrationMethodParametersViewModel : BaseViewModel() {
    private val _selectedMethodFlow = MutableStateFlow("")
    val selectedMethodFlow: StateFlow<String> = _selectedMethodFlow.asStateFlow()

    private val _accuracyFlow = MutableStateFlow(0.0)
    val accuracyFlow: StateFlow<Double> = _accuracyFlow.asStateFlow()

    private val _isAccuracyInUseFlow = MutableStateFlow(false)
    val isAccuracyInUseFlow: StateFlow<Boolean> = _isAccuracyInUseFlow.asStateFlow()

    private val _isStableAllowedInUseFlow = MutableStateFlow(false)
    val isStableAllowedInUseFlow: StateFlow<Boolean> = _isStableAllowedInUseFlow.asStateFlow()

    private val _isStableInUseFlow = MutableStateFlow(false)
    val isStableInUseFlow: StateFlow<Boolean> = _isStableInUseFlow.asStateFlow()

    private val _isParallelInUseFlow = MutableStateFlow(false)
    val isParallelInUseFlow: StateFlow<Boolean> = _isParallelInUseFlow.asStateFlow()

    private val _serverFlow = MutableStateFlow("")
    val serverFlow: StateFlow<String> = _serverFlow.asStateFlow()

    private val _portFlow = MutableStateFlow(0)
    val portFlow: StateFlow<Int> = _portFlow.asStateFlow()

    val selectedMethodProperty = SimpleStringProperty("").also {
        it.addListener { _, _, newValue -> _selectedMethodFlow.value = newValue as String }
    }
    var selectedMethod: String by selectedMethodProperty

    val accuracyProperty = SimpleDoubleProperty(0.0).also {
        it.addListener { _, _, newValue -> _accuracyFlow.value = newValue as Double }
    }
    var accuracy by accuracyProperty

    val isAccuracyInUseProperty = SimpleBooleanProperty(false).also {
        it.addListener { _, _, newValue -> _isAccuracyInUseFlow.value = newValue as Boolean }
    }
    var isAccuracyInUse by isAccuracyInUseProperty

    val isStableAllowedProperty = SimpleBooleanProperty(false).also {
        it.addListener { _, _, newValue -> _isStableAllowedInUseFlow.value = newValue as Boolean }
    }
    var isStableAllowedInUse by isStableAllowedProperty

    val isStableInUseProperty = SimpleBooleanProperty(false).also {
        it.addListener { _, _, newValue -> _isStableInUseFlow.value = newValue as Boolean }
    }
    var isStableInUse by isStableInUseProperty

    val isParallelInUseProperty = SimpleBooleanProperty(false).also {
        it.addListener { _, _, newValue -> _isParallelInUseFlow.value = newValue as Boolean }
    }
    var isParallelInUse by isParallelInUseProperty

    val serverProperty = SimpleStringProperty("").also {
        it.addListener { _, _, newValue -> _serverFlow.value = newValue as String }
    }
    var server: String by serverProperty

    val portProperty = SimpleIntegerProperty(0).also {
        it.addListener { _, _, newValue -> _portFlow.value = newValue as Int }
    }
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
