package models

import tornadofx.booleanProperty
import tornadofx.doubleProperty
import tornadofx.getValue
import tornadofx.setValue

class EventDetectionParametersModel {
    val isEventDetectionInUseProperty = booleanProperty()
    var isEventDetectionInUse by isEventDetectionInUseProperty

    val isStepLimitInUseProperty = booleanProperty()
    var isStepLimitInUse by isStepLimitInUseProperty

    val gammaProperty = doubleProperty()
    var gamma by gammaProperty

    val lowBorderProperty = doubleProperty()
    var lowBorder by lowBorderProperty
}