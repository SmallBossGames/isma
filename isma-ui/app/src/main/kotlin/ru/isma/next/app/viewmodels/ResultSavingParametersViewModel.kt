package ru.isma.next.app.viewmodels

import javafx.beans.property.SimpleObjectProperty
import ru.isma.next.services.simulation.abstractions.enumerables.SaveTarget
import ru.isma.next.services.simulation.abstractions.models.ResultSavingParametersModel
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