package ru.nstu.grin.concatenation.file.utilities

import ru.nstu.grin.concatenation.file.utilities.showError
import ru.nstu.grin.concatenation.function.model.FileType
import java.io.File

fun File.getFileType() =
    when (extension) {
        "xls" -> {
            FileType.XLS
        }
        "xlsx" -> {
            FileType.XLSX
        }
        "csv" -> {
            FileType.CSV
        }
        else -> {
            showError("Неправильный формат файла")
            null
        }
    }