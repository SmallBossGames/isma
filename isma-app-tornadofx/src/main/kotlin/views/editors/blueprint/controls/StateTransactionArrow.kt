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

    fun startXProperty() = startXProperty
    fun startYProperty() = startYProperty
    fun endXProperty() = endXProperty
    fun endYProperty() = endYProperty

    val startX by startXProperty
    val startY by startYProperty
    val endX by endXProperty
    val endY by endYProperty

    override val root: Parent = group {
        viewOrder = 4.0
        layoutXProperty().bind((endXProperty - startXProperty) / 2 + startXProperty)
        layoutYProperty().bind((endYProperty - startYProperty) / 2 + startYProperty)

        line {
            fun ss() {
                val x = this.endX - this.startX
                val y = this.endY - this.startY
                val angle = atan2(x, y) + PI/2
                val offsetX = 10.0 * sin(angle)
                val offsetY = 10.0 * cos(angle)

                startX = this@StateTransactionArrow.startX - this@group.layoutX + offsetX
                startY = this@StateTransactionArrow.startY - this@group.layoutY + offsetY
                endX = this@StateTransactionArrow.endX - this@group.layoutX + offsetX
                endY = this@StateTransactionArrow.endY - this@group.layoutY + offsetY
            }

            this@StateTransactionArrow.startXProperty.onChange { ss() }
            this@StateTransactionArrow.startYProperty.onChange { ss() }
            this@StateTransactionArrow.endXProperty.onChange { ss() }
            this@StateTransactionArrow.endYProperty.onChange { ss() }
        }
    }
}