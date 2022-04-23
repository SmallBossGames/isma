package ru.nstu.grin.concatenation.canvas.model

import ru.nstu.grin.common.model.Description
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction

sealed interface IMoveSettings {
    val pressedX: Double
    val pressedY: Double
}

data class FunctionMoveSettings(
    override val pressedX: Double,
    override val pressedY: Double,
    val function: ConcatenationFunction,
    val xAxis: ConcatenationAxis,
    val yAxis: ConcatenationAxis,
) : IMoveSettings

data class DescriptionMoveSettings(
    override val pressedX: Double,
    override val pressedY: Double,
    val description: Description,
) : IMoveSettings