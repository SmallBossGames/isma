package ru.nstu.grin.concatenation.dto

data class CartesianSpaceDTO(
    val functions: List<ConcatenationFunctionDTO>,
    val xAxis: ConcatenationAxisDTO,
    val yAxis: ConcatenationAxisDTO
)