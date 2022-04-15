package ru.nstu.grin.concatenation.axis.utilities

import javafx.scene.text.Font
import javafx.scene.text.Text
import ru.nstu.grin.concatenation.axis.controller.NumberFormatter
import ru.nstu.grin.concatenation.axis.model.AxisScaleProperties
import ru.nstu.grin.concatenation.axis.model.AxisScalingType

fun createStringValue(value: Double, axisScaleProperties: AxisScaleProperties) =
        if (axisScaleProperties.scalingType == AxisScalingType.LOGARITHMIC) {
                NumberFormatter.formatLogarithmic(value, axisScaleProperties.scalingLogBase)
        } else {
                NumberFormatter.format(value)
        }

fun estimateTextSize(text: String, font: Font) = Text(text)
        .apply { this.font = font }
        .run { Pair(layoutBounds.width, layoutBounds.height) }
