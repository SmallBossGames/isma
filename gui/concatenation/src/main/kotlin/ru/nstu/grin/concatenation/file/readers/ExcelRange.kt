package ru.nstu.grin.concatenation.file.readers

data class ExcelRange(
    val startCell: Int,
    val endCell: Int,
    val startRow: Int,
    val endRow: Int
)