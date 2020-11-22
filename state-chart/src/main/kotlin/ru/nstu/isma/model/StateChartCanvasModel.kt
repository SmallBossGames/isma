package ru.nstu.isma.model

import javafx.beans.property.SimpleListProperty
import javafx.collections.FXCollections
import javafx.scene.canvas.Canvas
import ru.nstu.isma.model.entity.StateElement
import tornadofx.ViewModel
import tornadofx.getValue
import tornadofx.*

class StateChartCanvasModel : ViewModel() {
    lateinit var canvas: Canvas

    var stateElementsProperty = SimpleListProperty<StateElement>(FXCollections.observableArrayList())
    var stateElements by stateElementsProperty
}