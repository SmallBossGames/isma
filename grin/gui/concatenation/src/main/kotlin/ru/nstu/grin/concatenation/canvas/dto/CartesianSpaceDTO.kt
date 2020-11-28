package ru.nstu.grin.concatenation.canvas.dto

import ru.nstu.grin.concatenation.axis.dto.ConcatenationAxisDTO
import ru.nstu.grin.concatenation.function.dto.ConcatenationFunctionDTO
import java.util.*

data class CartesianSpaceDTO(
    val id: UUID,
    val name: String,
    val functions: List<ConcatenationFunctionDTO>,
    val xAxis: ConcatenationAxisDTO,
    val yAxis: ConcatenationAxisDTO
)