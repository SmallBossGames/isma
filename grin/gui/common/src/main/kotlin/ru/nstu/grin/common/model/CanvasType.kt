package ru.nstu.grin.common.model

sealed class CanvasType

object ConcatenationType : CanvasType()

object SimpleType : CanvasType()