package models

import app.util.SaveTarget
import javafx.beans.property.SimpleObjectProperty
import tornadofx.getValue
import tornadofx.setValue

class ResultSavingParametersModel {
    val savingTargetProperty = SimpleObjectProperty<SaveTarget>()
    var savingTarget by savingTargetProperty
}