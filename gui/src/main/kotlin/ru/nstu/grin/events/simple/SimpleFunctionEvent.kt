package ru.nstu.grin.events.simple

import ru.nstu.grin.dto.simple.SimpleFunctionDTO
import tornadofx.FXEvent

data class SimpleFunctionEvent(
    val function: SimpleFunctionDTO
) : FXEvent()