package ru.nstu.grin.concatenation.events

import ru.nstu.grin.concatenation.dto.FunctionDTO
import tornadofx.FXEvent

class ConcatenationFunctionEvent(
    val function: FunctionDTO,
    val minAxisDelta: Double
) : FXEvent()