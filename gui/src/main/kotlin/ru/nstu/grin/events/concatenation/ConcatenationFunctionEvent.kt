package ru.nstu.grin.events.concatenation

import ru.nstu.grin.dto.concatenation.FunctionDTO
import tornadofx.FXEvent

class ConcatenationFunctionEvent(
    val functionDTO: FunctionDTO,
    val minAxisDelta: Double
) : FXEvent()