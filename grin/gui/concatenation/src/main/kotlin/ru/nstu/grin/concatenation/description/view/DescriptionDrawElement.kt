package ru.nstu.grin.concatenation.description.view

import javafx.geometry.VPos
import javafx.scene.canvas.GraphicsContext
import javafx.scene.text.TextAlignment
import ru.nstu.grin.common.view.ChainDrawElement
import ru.nstu.grin.concatenation.axis.utilities.estimateTextSize
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
        context.setLineDashes(2.0)

        for (space in canvasModel.cartesianSpaces){
            val xAxis = space.xAxis
            val yAxis = space.yAxis

            for (description in space.descriptions){
                context.stroke = description.color
                context.fill = description.color
                context.font = description.font



                val pointerX = matrixTransformer.transformUnitsToPixel(
                    description.pointerX,
                    xAxis.scaleProperties,
                    xAxis.direction,
                )

                val pointerY = matrixTransformer.transformUnitsToPixel(
                    description.pointerY,
                    yAxis.scaleProperties,
                    yAxis.direction,
                )

                val textX = description.textOffsetX + pointerX
                val textY = description.textOffsetY + pointerY

                val textSizes = estimateTextSize(description.text, description.font)

                context.strokeLine(textX, textY, pointerX, pointerY)
                context.clearRect(
                    textX - textSizes.first / 2.0,
                    textY - textSizes.second / 2.0,
                    textSizes.first,
                    textSizes.second,
                )
                context.clearRect(
                    pointerX - 2.0,
                    pointerY - 2.0,
                    4.0,
                    4.0
                )
                context.fillText(description.text, textX, textY)
                context.fillRect(pointerX - 2.0, pointerY - 2.0, 4.0, 4.0)
            }
        }

        context.restore()
    }
}