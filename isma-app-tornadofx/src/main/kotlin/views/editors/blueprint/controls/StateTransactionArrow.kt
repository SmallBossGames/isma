package views.editors.blueprint.controls

import javafx.beans.property.SimpleDoubleProperty
import javafx.scene.Parent
import tornadofx.*
import kotlin.math.*

class StateTransactionArrow : Fragment() {
    private val startXProperty = SimpleDoubleProperty(0.0)
    private val startYProperty = SimpleDoubleProperty(0.0)
    private val endXProperty = SimpleDoubleProperty(0.0)
    private val endYProperty = SimpleDoubleProperty(0.0)
//    private val centerXProperty = (endXProperty - startXProperty) / 2 + startXProperty
//    private val centerYProperty = (endYProperty - startYProperty) / 2 + startYProperty

    private val offsetXProperty = SimpleDoubleProperty(0.0)
    private val offsetYProperty = SimpleDoubleProperty(0.0)


    fun startXProperty() = startXProperty
    fun startYProperty() = startYProperty
    fun endXProperty() = endXProperty
    fun endYProperty() = endYProperty

    val startX by startXProperty
    val startY by startYProperty
    val endX by endXProperty
    val endY by endYProperty

    private val originalStartX = SimpleDoubleProperty()
    private val originalStartY = SimpleDoubleProperty()
    private val originalEndX = SimpleDoubleProperty()
    private val originalEndY = SimpleDoubleProperty()



    override val root: Parent = group {
        viewOrder = 4.0
        layoutXProperty().bind((endXProperty - startXProperty) / 2 + startXProperty)
        layoutYProperty().bind((endYProperty - startYProperty) / 2 + startYProperty)

        line {
            originalStartX.bind(this@StateTransactionArrow.startXProperty - this@group.layoutXProperty())
            originalStartY.bind(this@StateTransactionArrow.startYProperty - this@group.layoutYProperty())
            originalEndX.bind(this@StateTransactionArrow.endXProperty - this@group.layoutXProperty())
            originalEndY.bind(this@StateTransactionArrow.endYProperty - this@group.layoutYProperty())

            originalStartX.onChange { startX = it + calcAngleX(this) }
            originalStartY.onChange { startY = it + calcAngleY(this) }
            originalEndX.onChange { endX = it + calcAngleX(this) }
            originalEndY.onChange { endY = it + calcAngleY(this) }
        }
    }

    private fun calcAngleX(line: javafx.scene.shape.Line) : Double {
        val x = line.endX - line.startX
        val y = line.endY - line.startY

        val angle = atan2(x, y) + PI/2

        return 10.0 * sin(angle)
    }

    private fun calcAngleY(line: javafx.scene.shape.Line) : Double {
        val x = line.endX - line.startX
        val y = line.endY - line.startY

        val angle = atan2(x, y) + PI/2

        return 10.0 * cos(angle)
    }
}