package ru.nstu.grin.concatenation.axis.view

import javafx.geometry.VPos
import javafx.scene.canvas.GraphicsContext
import javafx.scene.text.TextAlignment
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.axis.model.MarksDistanceType
import ru.nstu.grin.concatenation.axis.utilities.estimateTextSize
import ru.nstu.grin.concatenation.canvas.controller.MatrixTransformer

class HorizontalAxisDrawStrategy(
    private val matrixTransformer: MatrixTransformer,
    private val pixelMarksArrayBuilder: HorizontalPixelMarksArrayBuilder,
    private val valueMarksArrayBuilder: HorizontalValueMarksArrayBuilder,
) : AxisMarksDrawStrategy {

    override fun drawMarks(
        context: GraphicsContext,
        axis: ConcatenationAxis,
        marksCoordinate: Double
    ) {
        context.save()

        val scaleProperties = axis.scaleProperties
        val styleProperties = axis.styleProperties

        context.stroke = styleProperties.marksColor
        context.fill = styleProperties.marksColor
        context.textAlign = TextAlignment.CENTER
        context.textBaseline = VPos.CENTER
        context.font = styleProperties.marksFont

        val (_, labelHeight) = estimateTextSize(axis.name, context.font)

        val marks = when(styleProperties.marksDistanceType){
            MarksDistanceType.PIXEL -> pixelMarksArrayBuilder
                .buildDoubleMarksArray(scaleProperties, styleProperties, axis.direction)
            MarksDistanceType.VALUE -> valueMarksArrayBuilder
                .buildDoubleMarksArray(scaleProperties, styleProperties, axis.direction)
        }

        val marksHeight = if (marks.isEmpty()) 0.0 else marks.maxOf { it.height }

        val offset = marksHeight - (labelHeight + marksHeight) / 2

        val marksY = marksCoordinate - DISTANCE_TO_LABEL / 2 - marksHeight / 2 + offset
        val labelY = marksCoordinate + DISTANCE_TO_LABEL / 2 + labelHeight / 2 + offset

        marks.forEach { context.fillText(it.text, it.x, marksY) }
        drawAxisLabel(context, axis, labelY)

        context.restore()
    }

    private fun drawAxisLabel(
        context: GraphicsContext,
        axis: ConcatenationAxis,
        labelCoordinate: Double,
    ){
        context.save()

        val (minPixel, maxPixel) = matrixTransformer.getMinMaxPixel(axis.direction)

        context.translate(minPixel + (maxPixel - minPixel) / 2, labelCoordinate)
        context.rotate(0.0)
        context.fillText(axis.name, 0.0, 0.0)

        context.restore()
    }

    private companion object {
        const val DISTANCE_TO_LABEL = 2.0
    }
}