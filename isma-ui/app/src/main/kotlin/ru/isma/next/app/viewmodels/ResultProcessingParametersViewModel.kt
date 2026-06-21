package ru.isma.next.app.viewmodels

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleStringProperty
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.isma.javafx.extensions.viewmodel.BaseViewModel
import ru.isma.javafx.extensions.helpers.getValue
import ru.isma.javafx.extensions.helpers.setValue

class ResultProcessingParametersViewModel : BaseViewModel() {
    private val _isSimplifyInUseFlow = MutableStateFlow(false)
    val isSimplifyInUseFlow: StateFlow<Boolean> = _isSimplifyInUseFlow.asStateFlow()

    private val _selectedSimplifyMethodFlow = MutableStateFlow("")
    val selectedSimplifyMethodFlow: StateFlow<String> = _selectedSimplifyMethodFlow.asStateFlow()

    private val _toleranceFlow = MutableStateFlow(0.0)
    val toleranceFlow: StateFlow<Double> = _toleranceFlow.asStateFlow()

    val isSimplifyInUseProperty = SimpleBooleanProperty(false).also {
        it.addListener { _, _, newValue -> _isSimplifyInUseFlow.value = newValue as Boolean }
    }
    var isSimplifyInUse by isSimplifyInUseProperty

    val selectedSimplifyMethodProperty = SimpleStringProperty("").also {
        it.addListener { _, _, newValue -> _selectedSimplifyMethodFlow.value = newValue as String }
    }
    var selectedSimplifyMethod: String by selectedSimplifyMethodProperty

    val toleranceProperty = SimpleDoubleProperty(0.0).also {
        it.addListener { _, _, newValue -> _toleranceFlow.value = newValue as Double }
    }
    var tolerance by toleranceProperty

    fun isSimplifyInUseProperty() = isSimplifyInUseProperty
    fun selectedSimplifyMethodProperty() = selectedSimplifyMethodProperty
    fun toleranceProperty() = toleranceProperty
}
