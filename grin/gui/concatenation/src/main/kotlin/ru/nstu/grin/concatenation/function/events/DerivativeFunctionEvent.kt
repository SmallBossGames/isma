package ru.nstu.grin.concatenation.function.events

import ru.nstu.grin.concatenation.function.model.DerivativeType
import tornadofx.FXEvent
import java.util.*

data class DerivativeFunctionEvent(
    val id: UUID,
    val type: DerivativeType,
    val degree: Int
) : FXEvent()