package ru.nstu.grin.concatenation.function.model

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.scene.paint.Color
import ru.isma.javafx.extensions.helpers.getValue
import ru.isma.javafx.extensions.helpers.setValue

class ChangeFunctionViewModel(val function: ConcatenationFunction) {
    val nameProperty = SimpleStringProperty()
    var name by nameProperty

    val functionColorProperty = SimpleObjectProperty<Color>()
    var functionColor by functionColorProperty

    val lineSizeProperty = SimpleDoubleProperty()
    var lineSize by lineSizeProperty

    val lineTypeProperty = SimpleObjectProperty<LineType>()
    var lineType by lineTypeProperty

    val isHideProperty = SimpleBooleanProperty()
    var isHide by isHideProperty

    val modifiers = FXCollections.observableArrayList<IAsyncPointsTransformerViewModel>()

    fun addModifier(factory: () -> IAsyncPointsTransformerViewModel){
        modifiers.add(factory())
    }

    fun removeModifier(item: IAsyncPointsTransformerViewModel){
        modifiers.remove(item)
    }

    init {
        name = function.name
        functionColor = function.functionColor
        lineSize = function.lineSize
        lineType = function.lineType
        isHide = function.isHide

        modifiers.setAll(function.transformers.map { it.toViewModel() })
    }
}