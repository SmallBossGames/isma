package ru.nstu.grin.concatenation.file.excel

data class ExcelRange(
    val startCell: Int,
    val endCell: Int,
    val startRow: Int,
    val endRow: Int
)