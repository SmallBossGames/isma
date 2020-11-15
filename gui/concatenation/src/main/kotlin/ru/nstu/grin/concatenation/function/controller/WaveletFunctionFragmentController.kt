package ru.nstu.grin.concatenation.function.controller

import ru.nstu.grin.concatenation.function.events.WaveletFunctionEvent
import ru.nstu.grin.concatenation.function.model.WaveletFunctionFragmentModel
import tornadofx.Controller
import java.util.*

class WaveletFunctionFragmentController : Controller() {
    private val model: WaveletFunctionFragmentModel by inject()
    val functionId: UUID by param()

    fun enableWavelet() {
        val event = WaveletFunctionEvent(
            id = functionId,
            waveletDirection = model.waveletDirection,
            waveletTransformFun = model.waveletTransformFun
        )
        fire(event)
    }
}