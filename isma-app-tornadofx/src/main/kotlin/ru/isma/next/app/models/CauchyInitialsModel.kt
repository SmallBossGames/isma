package ru.isma.next.app.models

import tornadofx.*
import javafx.beans.property.SimpleDoubleProperty

class CauchyInitialsModel {
    val startTimeProperty = SimpleDoubleProperty()
    var startTime by startTimeProperty

    val endTimeProperty = SimpleDoubleProperty()
    var endTime by endTimeProperty

    val stepProperty = SimpleDoubleProperty()
    var step by stepProperty
}