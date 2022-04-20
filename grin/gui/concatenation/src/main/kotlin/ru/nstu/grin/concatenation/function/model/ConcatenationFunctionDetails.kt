package ru.nstu.grin.concatenation.function.model

import ru.nstu.grin.common.model.WaveletDirection
import ru.nstu.grin.common.model.WaveletTransformFun

sealed class ConcatenationFunctionDetails

data class DerivativeDetails(
    val degree: Int,
    val type: DerivativeType
) : ConcatenationFunctionDetails()

data class WaveletDetails(
    val waveletTransformFun: WaveletTransformFun,
    val waveletDirection: WaveletDirection
) : ConcatenationFunctionDetails()