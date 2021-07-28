package ru.isma.next.app.viewmodels

import ru.isma.next.app.enumerables.SaveTarget
import javafx.beans.property.SimpleObjectProperty
import ru.isma.next.app.models.simulation.ResultSavingParametersModel
import tornadofx.getValue
import tornadofx.setValue

class ResultSavingParametersViewModel {
    val savingTargetProperty = SimpleObjectProperty(SaveTarget.MEMORY)
    var savingTarget: SaveTarget by savingTargetProperty

    fun commit(model: ResultSavingParametersModel){
        savingTarget = model.savingTarget
    }

    fun snapshot() = ResultSavingParametersModel(savingTarget)
}