package ru.nstu.grin.concatenation.axis.utilities

import javafx.scene.text.Font
import javafx.scene.text.Text
import ru.nstu.grin.concatenation.axis.controller.NumberFormatter
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis

fun createStringValue(value: Double, axis: ConcatenationAxis) =
        if (axis.isLogarithmic()) {
                NumberFormatter.formatLogarithmic(value, axis.settings.logarithmBase)
        } else {
                NumberFormatter.format(value)
        }

fun estimateTextSize(text: String, font: Font) = Text(text)
        .apply { this.font = font }
        .run { Pair(layoutBounds.width, layoutBounds.height) }
