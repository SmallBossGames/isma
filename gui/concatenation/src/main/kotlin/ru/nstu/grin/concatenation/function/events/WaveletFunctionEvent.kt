package ru.nstu.grin.concatenation.function.events

import ru.nstu.grin.common.model.WaveletDirection
import ru.nstu.grin.common.model.WaveletTransformFun
import tornadofx.FXEvent
import java.util.*

data class WaveletFunctionEvent(
    val id: UUID,
    val waveletTransformFun: WaveletTransformFun,
    val waveletDirection: WaveletDirection
) : FXEvent()