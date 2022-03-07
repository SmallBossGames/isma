package ru.nstu.grin.concatenation.description.model

import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.paint.Color
import ru.isma.javafx.extensions.helpers.getValue
import ru.isma.javafx.extensions.helpers.setValue
import ru.nstu.grin.common.model.Description

class ChangeDescriptionViewModel(
    val description: Description? = null,
    initData: DescriptionModalInitData? = null,
) {
    val xPositionProperty = SimpleDoubleProperty( initData?.xPosition ?: description?.x ?: 0.0)
    var xPosition by xPositionProperty

    val yPositionProperty = SimpleDoubleProperty(initData?.yPosition ?: description?.y ?: 0.0)
    var yPosition by yPositionProperty

    val textProperty = SimpleStringProperty(description?.text ?: "")
    var text: String by textProperty

    val textSizeProperty = SimpleDoubleProperty(description?.textSize ?: 12.0)
    var textSize by textSizeProperty

    val colorProperty = SimpleObjectProperty(description?.color ?: Color.BLACK)
    var color: Color by colorProperty

    val fontProperty = SimpleStringProperty(description?.font ?: "Arial")
    var font: String by fontProperty
}

