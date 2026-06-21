package ru.isma.next.app.viewmodels

import javafx.beans.property.SimpleObjectProperty
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.isma.javafx.extensions.viewmodel.BaseViewModel
import ru.isma.javafx.extensions.helpers.getValue
import ru.isma.javafx.extensions.helpers.setValue
import ru.isma.next.app.models.simulation.SaveTarget
import ru.isma.next.app.models.simulation.ResultSavingParametersModel

class ResultSavingParametersViewModel : BaseViewModel() {
    private val _savingTargetFlow = MutableStateFlow(SaveTarget.MEMORY)
    val savingTargetFlow: StateFlow<SaveTarget> = _savingTargetFlow.asStateFlow()

    val savingTargetProperty = SimpleObjectProperty(SaveTarget.MEMORY).also {
        it.addListener { _, _, newValue -> _savingTargetFlow.value = newValue as SaveTarget }
    }
    var savingTarget: SaveTarget by savingTargetProperty

    fun savingTargetProperty() = savingTargetProperty

    fun commit(model: ResultSavingParametersModel){
        savingTarget = model.savingTarget
    }

    fun snapshot() = ResultSavingParametersModel(savingTarget)
}
