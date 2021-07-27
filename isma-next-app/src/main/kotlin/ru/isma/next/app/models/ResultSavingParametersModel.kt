package ru.isma.next.app.models

import ru.isma.next.app.enumerables.SaveTarget
import javafx.beans.property.SimpleObjectProperty
import tornadofx.getValue
import tornadofx.setValue

class ResultSavingParametersModel {
    val savingTargetProperty = SimpleObjectProperty(SaveTarget.MEMORY)
    var savingTarget: SaveTarget by savingTargetProperty
}