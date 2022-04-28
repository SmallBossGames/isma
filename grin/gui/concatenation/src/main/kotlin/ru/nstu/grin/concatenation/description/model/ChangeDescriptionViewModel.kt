package ru.nstu.grin.concatenation.description.model

import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.paint.Color
import ru.isma.javafx.extensions.helpers.getValue
import ru.isma.javafx.extensions.helpers.setValue
import ru.nstu.grin.concatenation.canvas.controller.MatrixTransformer
import ru.nstu.grin.concatenation.cartesian.model.CartesianSpace

class ChangeDescriptionViewModel(
    initData: IDescriptionModalInitData,
    matrixTransformer: MatrixTransformer,
) {
    val description = (initData as? DescriptionModalForUpdate)?.description

    val cartesianSpaceProperty = SimpleObjectProperty<CartesianSpace>()
    var cartesianSpace by cartesianSpaceProperty

    val xPositionProperty = SimpleDoubleProperty()
    var xPosition by xPositionProperty

    val yPositionProperty = SimpleDoubleProperty()
    var yPosition by yPositionProperty

    val textOffsetXProperty = SimpleDoubleProperty()
    var textOffsetX by textOffsetXProperty

    val textOffsetYProperty = SimpleDoubleProperty()
    var textOffsetY by textOffsetYProperty

    val textProperty = SimpleStringProperty()
    var text: String by textProperty

    val colorProperty = SimpleObjectProperty<Color>()
    var color: Color by colorProperty

    val textSizeProperty = SimpleDoubleProperty()
    var textSize by textSizeProperty

    val fontProperty = SimpleStringProperty()
    var font: String by fontProperty

    init {
        when(initData){
            is DescriptionModalForCreate -> {
                cartesianSpace = initData.cartesianSpace
                xPosition = initData.xPosition
                yPosition = initData.yPosition
                textOffsetX = 30.0
                textOffsetY = 30.0
                text = ""
                color = Color.BLACK
                textSize = 12.0
                font = "Arial"
            }
            is DescriptionModalForUpdate -> {
                cartesianSpace = initData.cartesianSpace
                xPosition = matrixTransformer.transformUnitsToPixel(
                    initData.description.pointerX,
                    initData.cartesianSpace.xAxis.scaleProperties,
                    initData.cartesianSpace.xAxis.direction
                )
                yPosition = matrixTransformer.transformUnitsToPixel(
                    initData.description.pointerY,
                    initData.cartesianSpace.yAxis.scaleProperties,
                    initData.cartesianSpace.yAxis.direction
                )
                textOffsetX = initData.description.textOffsetX
                textOffsetY = initData.description.textOffsetY
                text = initData.description.text
                color = initData.description.color
                textSize = initData.description.font.size
                font = initData.description.font.family
            }
        }
    }
}

