package ru.nstu.grin.concatenation.function.model

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonSubTypes(
    value = [
        JsonSubTypes.Type(value = MirrorDetails::class),
        JsonSubTypes.Type(value = DerivativeDetails::class)
    ]
)
sealed class ConcatenationFunctionDetails

data class MirrorDetails(
    val isMirrorX: Boolean = false,
    val isMirrorY: Boolean = false
) : ConcatenationFunctionDetails()

data class DerivativeDetails(
    val degree: Int,
    val type: DerivativeType
) : ConcatenationFunctionDetails()