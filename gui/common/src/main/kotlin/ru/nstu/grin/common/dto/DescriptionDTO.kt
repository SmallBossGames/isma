package ru.nstu.grin.common.dto

import javafx.scene.paint.Color

/**
 * @author kostya05983
 */
data class DescriptionDTO(
    val x: Double,
    val y: Double,
    val text: String,
    val size: Double,
    val textColor: Color
)