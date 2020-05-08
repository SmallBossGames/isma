package ru.nstu.grin.concatenation.canvas.events

import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import tornadofx.FXEvent

data class GetAllFunctionsEvent(
    val functions: List<ConcatenationFunction>
): FXEvent()