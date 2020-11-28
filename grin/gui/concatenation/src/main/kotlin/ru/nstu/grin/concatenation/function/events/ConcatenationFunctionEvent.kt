package ru.nstu.grin.concatenation.function.events

import ru.nstu.grin.concatenation.canvas.dto.CartesianSpaceDTO
import tornadofx.FXEvent

class ConcatenationFunctionEvent(
    val cartesianSpace: CartesianSpaceDTO
) : FXEvent()