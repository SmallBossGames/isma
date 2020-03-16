package ru.nstu.grin.concatenation.events

import ru.nstu.grin.concatenation.dto.CartesianSpaceDTO
import tornadofx.FXEvent

class ConcatenationFunctionEvent(
    val cartesianSpace: CartesianSpaceDTO,
    val minAxisDelta: Double
) : FXEvent()