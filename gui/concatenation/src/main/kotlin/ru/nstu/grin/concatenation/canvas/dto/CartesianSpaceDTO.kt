package ru.nstu.grin.concatenation.canvas.dto

import ru.nstu.grin.concatenation.axis.dto.ConcatenationAxisDTO
import ru.nstu.grin.concatenation.function.dto.ConcatenationFunctionDTO

data class CartesianSpaceDTO(
    val functions: List<ConcatenationFunctionDTO>,
    val xAxis: ConcatenationAxisDTO,
    val yAxis: ConcatenationAxisDTO
)