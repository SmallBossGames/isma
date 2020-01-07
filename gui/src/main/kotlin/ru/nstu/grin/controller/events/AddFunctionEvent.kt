package ru.nstu.grin.controller.events

import ru.nstu.grin.dto.FunctionDTO
import tornadofx.FXEvent

class AddFunctionEvent(
    val functionDTO: FunctionDTO,
    val minAxisDelta: Double
) : FXEvent()