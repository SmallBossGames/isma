package ru.nstu.grin.concatenation.description.view

import javafx.geometry.VPos
import javafx.scene.canvas.GraphicsContext
import javafx.scene.text.Font
import javafx.scene.text.TextAlignment
import ru.nstu.grin.common.view.ChainDrawElement
import ru.nstu.grin.concatenation.canvas.controller.MatrixTransformer
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel

class DescriptionDrawElement(
    private val canvasModel: ConcatenationCanvasModel,
    private val matrixTransformer: MatrixTransformer,
) : ChainDrawElement {
    override fun draw(context: GraphicsContext, canvasWidth: Double, canvasHeight: Double) {
        context.save()

        context.textAlign = TextAlignment.CENTER
        context.textBaseline = VPos.CENTER

        for (space in canvasModel.cartesianSpaces){
            val xAxis = space.xAxis
            val yAxis = space.yAxis

            for (description in space.descriptions){
                context.stroke = description.color
                context.fill = description.color
                context.font = Font.font(description.font, description.textSize)

                val x = matrixTransformer.transformUnitsToPixel(
                    description.x,
                    xAxis.scaleProperties,
                    xAxis.direction,
                )

                val y = matrixTransformer.transformUnitsToPixel(
                    description.y,
                    yAxis.scaleProperties,
                    yAxis.direction,
                )

                context.fillText(description.text, x, y)
            }
        }

        context.restore()
    }
}