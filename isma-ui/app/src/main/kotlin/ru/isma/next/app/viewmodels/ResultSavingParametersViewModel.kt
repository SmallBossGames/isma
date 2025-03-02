package ru.isma.next.app.viewmodels

import javafx.beans.property.SimpleObjectProperty
import ru.isma.next.app.models.simulation.SaveTarget
import ru.isma.next.app.models.simulation.ResultSavingParametersModel
import ru.isma.javafx.extensions.helpers.getValue
import ru.isma.javafx.extensions.helpers.setValue

class ResultSavingParametersViewModel {
    val savingTargetProperty = SimpleObjectProperty(SaveTarget.MEMORY)
    var savingTarget: SaveTarget by savingTargetProperty

    fun commit(model: ResultSavingParametersModel){
        savingTarget = model.savingTarget
    }

    fun snapshot() = ResultSavingParametersModel(savingTarget)
}