package ru.nstu.grin.simple.events

import ru.nstu.grin.simple.dto.SimpleFunctionDTO
import tornadofx.FXEvent

data class SimpleFunctionEvent(
    val function: SimpleFunctionDTO
) : FXEvent()