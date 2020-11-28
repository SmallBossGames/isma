package ru.nstu.grin.concatenation.file.readers

import ru.nstu.grin.concatenation.function.model.FileType
import java.io.File

object FileRecognizer {
    fun recognize(file: File): FileType {
        val fileExtension = file.name.substringAfterLast(".")
        return when (fileExtension) {
            "csv" -> {
                FileType.CSV
            }
            "xls" -> {
                FileType.XLS
            }
            "xlsx" -> {
                FileType.XLSX
            }
            else -> FileType.UNKNOWN
        }
    }
}