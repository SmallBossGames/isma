package ru.nstu.grin.concatenation.canvas.model

import ru.nstu.grin.common.model.Description
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction

sealed interface IMoveSettings {
    val currentX: Double
    val currentY: Double
}

data class FunctionMoveSettings(
    override var currentX: Double,
    override var currentY: Double,
    val function: ConcatenationFunction,
    val xAxis: ConcatenationAxis,
    val yAxis: ConcatenationAxis,
) : IMoveSettings

data class DescriptionMoveSettings(
    override val currentX: Double,
    override val currentY: Double,
    val description: Description,
) : IMoveSettings