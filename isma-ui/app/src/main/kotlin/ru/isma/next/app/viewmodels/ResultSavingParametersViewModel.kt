package ru.isma.next.app.viewmodels

import javafx.beans.property.SimpleObjectProperty
import ru.isma.next.app.models.simulation.SaveTarget
import ru.isma.next.app.models.simulation.ResultSavingParametersModel

class ResultSavingParametersViewModel {
    val savingTargetProperty = SimpleObjectProperty(SaveTarget.MEMORY)
    var savingTarget: SaveTarget
        get() = savingTargetProperty.value
        set(value) { savingTargetProperty.value = value }

    fun commit(model: ResultSavingParametersModel){
        savingTarget = model.savingTarget
    }

    fun snapshot() = ResultSavingParametersModel(savingTarget)
}
