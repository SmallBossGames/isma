package ru.nstu.grin.model

sealed class CanvasType

object ConcatenationType : CanvasType()

object SimpleType : CanvasType()