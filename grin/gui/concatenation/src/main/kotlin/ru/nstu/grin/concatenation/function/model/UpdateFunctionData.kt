package ru.nstu.grin.concatenation.function.model

import javafx.scene.paint.Color

data class UpdateFunctionData(
    val function: ConcatenationFunction,
    val name: String,
    val color: Color,
    val lineSize: Double,
    val lineType: LineType,
    val isHide: Boolean,
    val mirrorDetails: MirrorDetails
)