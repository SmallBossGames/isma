package ru.nstu.grin.model.view

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.paint.Color
import tornadofx.*

class ManualEnterFunctionViewModel : ViewModel() {
    var textProperty = SimpleStringProperty()
    var text: String by textProperty

    var xDirectionProperty = SimpleStringProperty()
    var xDirection: String by xDirectionProperty

    var yDirectionProperty = SimpleStringProperty()
    var yDirection: String by yDirectionProperty

    var functionColorProperty = SimpleObjectProperty<Color>()
    var functionColor by functionColorProperty

    var xAxisColorProperty = SimpleObjectProperty<Color>()
    var xAxisColor by xAxisColorProperty

    var xDelimeterColorProperty = SimpleObjectProperty<Color>()
    var xDelimiterColor by xDelimeterColorProperty

    var yAxisColorProperty = SimpleObjectProperty<Color>()
    var yAxisColor by yAxisColorProperty

    var yDelimiterColorProperty = SimpleObjectProperty<Color>()
    var yDelimeterColor by yDelimiterColorProperty
}