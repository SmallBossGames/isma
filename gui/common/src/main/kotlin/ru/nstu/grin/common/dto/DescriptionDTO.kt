package ru.nstu.grin.common.dto

import javafx.scene.paint.Color
import java.util.*

/**
 * @author kostya05983
 */
data class DescriptionDTO(
    val id: UUID,
    val x: Double,
    val y: Double,
    val text: String,
    val size: Double,
    val textColor: Color,
    val font: String
)