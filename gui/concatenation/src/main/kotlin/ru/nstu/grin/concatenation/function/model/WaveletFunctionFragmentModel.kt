package ru.nstu.grin.concatenation.function.model

import javafx.beans.property.SimpleObjectProperty
import ru.nstu.grin.common.model.WaveletDirection
import ru.nstu.grin.common.model.WaveletTransformFun
import tornadofx.ViewModel
import tornadofx.*

class WaveletFunctionFragmentModel : ViewModel() {
    val waveletTransformFunProperty = SimpleObjectProperty(WaveletTransformFun.COIFLET5)
    val waveletTransformFun by waveletTransformFunProperty

    val waveletDirectionProperty = SimpleObjectProperty(WaveletDirection.BOTH)
    val waveletDirection by waveletDirectionProperty
}